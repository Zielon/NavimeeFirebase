package com.navimee.repositories;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.firestore.FirebasePaths;
import com.navimee.firestore.PathBuilder;
import com.navimee.firestore.operations.DbDelete;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.navimee.firestore.FirebasePaths.FRIENDS;
import static com.navimee.firestore.FirebasePaths.GROUP;
import static com.navimee.firestore.FirebasePaths.USERS;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    @Autowired
    Firestore database;

    @Autowired
    DbGet dbGet;

    @Autowired
    DbDelete dbDelete;

    @Autowired
    ExecutorService executorService;

    @Override
    public CompletableFuture<User> getUser(String id) {
        return dbGet.fromDocument(database.collection(USERS).document(id), User.class);
    }

    @Override
    public CompletableFuture<List<User>> getAllUsers() {
        return dbGet.fromCollection(database.collection(USERS), User.class);
    }

    @Override
    public CompletableFuture<Void> updateUsersField(String fieldName, Object value) throws NoSuchFieldException {
        return CompletableFuture.runAsync(
                () -> getAllUsers().join().forEach(
                        user -> database.document(new PathBuilder().add(USERS).add(user.getId()).build())
                                .update(fieldName, value)), executorService);
    }

    @Override
    public CompletableFuture<Void> deleteUsersField(String fieldName) throws NoSuchFieldException {
        return CompletableFuture.runAsync(
                () -> getAllUsers().join().forEach(
                        user -> database.document(new PathBuilder().add(USERS).add(user.getId()).build())
                                .update(fieldName, FieldValue.delete())), executorService);
    }

    @Override
    public CompletableFuture<Void> deleteUsersCollection(String collection) {
        return CompletableFuture.runAsync(
                () -> getAllUsers().join().forEach(
                        user -> {
                            CollectionReference reference = database.collection(new PathBuilder().add(USERS).add(user.getId()).add(collection).build());
                            dbDelete.collection(reference, 0);
                        }), executorService);
    }

    @Override
    public CompletableFuture<Void> deleteUser(String id) {
        return CompletableFuture.runAsync(
                () -> {
                    dbDelete.collection(database.collection(new PathBuilder().add(USERS).add(id).add(FRIENDS).build()));
                    dbDelete.collection(database.collection(new PathBuilder().add(USERS).add(id).add(GROUP).build()));
                    database.document(new PathBuilder().add(USERS).add(id).build()).delete();
                }, executorService);
    }

    @Override
    public CompletableFuture<List<User>> getUsersWithBigEventsOn() {
        Query query = database.collection(USERS).whereEqualTo("bigEventsNotification", true);
        return dbGet.fromQuery(query, User.class);
    }
}