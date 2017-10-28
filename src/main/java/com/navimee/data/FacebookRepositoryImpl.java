package com.navimee.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class FacebookRepositoryImpl implements FacebookRepository {

    private static final String eventsPath = "events";

    @Autowired
    @Qualifier("dbContext")
    DatabaseReference dbContext;

    @Override
    public void addEvent(Event event) {
        dbContext.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dbContext.child(eventsPath).push().setValueAsync(event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void updateEvent(Event event) {

    }
}
