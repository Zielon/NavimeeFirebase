package com.navimee.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.navimee.configuration.FirebaseInitialization;
import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.models.Event;
import com.navimee.models.Place;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Repository
public class FacebookRepositoryImpl implements FacebookRepository {

    // PATHS
    private final String eventsPath = "events";
    private final String placesPath = "places";
    private final String historicalEventsPath = "historicalEvents";

    private final DatabaseReference dbContext = FirebaseInitialization.getDatabaseReference();

    @Override
    public void setEvents(List<Event> events) {
        Map<String, Event> map = new HashMap<>();
        events.stream().forEach(e -> map.put(e.id, e));

        dbContext.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dbContext.child(eventsPath).setValueAsync(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void addPlaces(List<Place> places) {
        Map<String, Place> map = new HashMap<>();
        places.forEach(p -> map.put(p.id, p));

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
    public void updateEvents(List<Event> events) {
        Map<String, Object> newEventsMap = new HashMap<>();
        events.stream().forEach(e -> newEventsMap.put(e.id, e));

        dbContext.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dbContext.child(eventsPath).updateChildrenAsync(newEventsMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void updateHistorical(List<Event> events) {
        Map<String, Object> historicalEvents = new HashMap<>();
        events.stream().forEach(e -> historicalEvents.put(e.id, e));

        dbContext.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dbContext.child(historicalEventsPath).updateChildrenAsync(historicalEvents);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public Future<List<Event>> getEvents() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        List<Event> events = new ArrayList<>();

        return executor.submit(() -> {
            dbContext.child(eventsPath).addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot snapshot) {
                      if(snapshot.exists())
                          snapshot.getChildren().forEach(e -> events.add(e.getValue(Event.class)));
                  }
                  @Override
                  public void onCancelled(DatabaseError databaseError) {}
              });
        }, events);
    }
}