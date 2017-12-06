package com.navimee.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.firestore.Paths;
import com.navimee.firestore.operations.Get;
import com.navimee.models.entities.User;
import com.navimee.models.entities.events.FbEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.navimee.firestore.Paths.USERS_COLLECTION;
import static com.navimee.firestore.Paths.USERS_EVENTS_COLLECTION;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    @Autowired
    Firestore db;

    @Autowired
    Get get;

    @Override
    public List<User> getUsers() {
        ObjectMapper mapper = new ObjectMapper();
        List<User> users = new ArrayList<>();
        try {
            for (DocumentSnapshot document : db.collection(USERS_COLLECTION).get().get().getDocuments()) {
                User user = mapper.convertValue(document.getData(), User.class);
                user.setReference(Paths.get(db.collection(USERS_COLLECTION)));
                user.setEvents(get.fromCollection(document.getReference().collection(USERS_EVENTS_COLLECTION), FbEvent.class));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }
}
