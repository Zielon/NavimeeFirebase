package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.navimee.enums.CollectionType.USERS;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    @Autowired
    Firestore db;

    @Autowired
    DbGet dbGet;

    @Override
    public User getUser(String id) {
        return dbGet.fromSingleDocument(db.collection(USERS.toString()).document(id), User.class);
    }
}
