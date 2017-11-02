package com.navimee.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.navimee.FirebaseInitialization;
import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.models.Event;
import com.navimee.models.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Repository
public class FacebookRepositoryImpl implements FacebookRepository {

    private static final String eventsPath = "events";
    private static final String placesPath = "places";


    @Override
    public void addEvents(List<Event> events) {

        try{
            Map<String, Event> map = new HashMap<>();
            events.stream().sorted(Comparator.comparing(e2 -> e2.start_time)).forEach(e -> map.put(e.id, e));
            DatabaseReference dbContext = FirebaseInitialization.getDatabaseReference();
            dbContext.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    dbContext.child(eventsPath).setValueAsync(map);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();
            NavimeeApplication.logs.add(sStackTrace);
        }
    }

    @Override
    public void addPlaces(List<Place> places) {
        Map<String, Place> map = new HashMap<>();
        places.forEach(p -> map.put(p.id, p));
        DatabaseReference dbContext = FirebaseInitialization.getDatabaseReference();
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