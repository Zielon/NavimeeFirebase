package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.navimee.firestore.FirebasePaths.USERS;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    @Autowired
    Firestore database;

    @Autowired
    DbGet dbGet;

    @Override
    public CompletableFuture<User> getUser(String id) {
        return dbGet.fromDocument(database.collection(USERS).document(id), User.class);
    }

    @Override
    public CompletableFuture<List<User>> getUsersWithBigEventsOn() {
        Query query = database.collection(USERS).whereEqualTo("bigEventsNotification", true);
        return dbGet.fromQuery(query, User.class);
    }
}