package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.navimee.firestore.Paths.USERS_COLLECTION;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    @Autowired
    Firestore db;

    @Autowired
    DbGet dbGet;

    @Override
    public User getUser(String id) {
        User user = dbGet.fromSingleDocument(db.collection(USERS_COLLECTION).document(id), User.class);
        user.setId(id);
        return user;
    }
}