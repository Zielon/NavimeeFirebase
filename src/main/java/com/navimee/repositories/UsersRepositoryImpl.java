package com.navimee.repositories;

import com.google.cloud.firestore.Query;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.navimee.enums.CollectionType.USERS;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    @Autowired
    Database database;

    @Autowired
    DbGet dbGet;

    @Override
    public User getUser(String id) {
        return dbGet.fromSingleDocument(database.getCollection(USERS).document(id), User.class);
    }

    @Override
    public List<User> getUsersWithBigEventsOn() {
        Query query = database.getCollection(USERS).whereEqualTo("bigEventsNotification", true);
        return dbGet.fromCollection(query, User.class);
    }
}