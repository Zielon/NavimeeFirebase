package com.navimee.navimee.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.navimee.navimee.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wojtek on 2017-10-23.
 */
@Service
public class FirebaseServiceImpl implements FirebaseService {

    @Autowired
    @Qualifier("main")
    DatabaseReference mainDatabaseReference;



    @Value("${firebase.path}")
    private String chatPath;

    @Override
    public void startFirebaseListener() {
        mainDatabaseReference.child(chatPath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Zmiana na bazie:< ");
                 /*
                 TODO: Here put your code
                 */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Cos nie pyklo :< ");
            }
        });


    }

    @Override
    public void saveEvents() {
        Map<String, Event> users = new HashMap<>();
        users.put("alanisawesome", new Event((int)(Math.random() * 100),"June 23, 1912", "Alan Turing","Hello"));
        users.put("gracehop", new Event((int)(Math.random() * 100),"December 9, 1906", "Grace Hopper","Hello"));

        mainDatabaseReference.child(chatPath).setValueAsync(users);
    }
}