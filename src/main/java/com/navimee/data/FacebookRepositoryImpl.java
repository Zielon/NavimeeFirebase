package com.navimee.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.models.Event;
import com.navimee.models.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FacebookRepositoryImpl implements FacebookRepository {

    private static final String eventsPath = "events";
    private static final String placesPath = "places";


    @Override
    public void addEvents(List<Event> events) {
        Map<String, Event> map = new HashMap<>();
        DatabaseReference dbContext = NavimeeApplication.getDatabaseReference();
        events.stream().sorted(Comparator.comparing(e2 -> e2.start_time)).forEach(e -> map.put(e.id, e));
        dbContext.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dbContext.child(eventsPath).setValueAsync(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void addPlaces(List<Place> places) {
        Map<String, Place> map = new HashMap<>();
        places.forEach(p -> map.put(p.id, p));
        DatabaseReference dbContext = NavimeeApplication.getDatabaseReference();
        dbContext.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dbContext.child(placesPath).setValueAsync(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void updateEvents(List<Event> events) { }
}