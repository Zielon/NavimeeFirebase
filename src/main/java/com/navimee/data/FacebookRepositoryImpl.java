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

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class FacebookRepositoryImpl implements FacebookRepository {

    private final DatabaseReference dbContext = FirebaseInitialization.getDatabaseReference();

    @Override
    public void setEvents(List<Event> events) {
        Map<String, Event> map =
                events.stream().collect(Collectors.toMap(Event::getId, Function.identity()));

        dbContext.child(eventsPath).setValueAsync(map);
    }

    @Override
    public void setPlaces(List<Place> places) {
        Map<String, Place> map =
                places.stream().collect(Collectors.toMap(Place::getId, Function.identity()));

        dbContext.child(placesPath).setValueAsync(map);
    }

    @Override
    public void updateEvents(List<Event> events) {
        Map<String, Object> newEventsMap =
                events.stream().collect(Collectors.toMap(Event::getId, Function.identity()));

        dbContext.child(eventsPath).updateChildrenAsync(newEventsMap);
    }

    @Override
    public void updateHistorical(List<Event> events) {
        Map<String, Object> historicalEvents =
                events.stream().collect(Collectors.toMap(Event::getId, Function.identity()));

        dbContext.child(historicalEventsPath).updateChildrenAsync(historicalEvents);
    }

    @Override
    public void removeEvents(List<Event> events) {
        Map<String, Object> eventsToRemove =
                events.stream().collect(Collectors.toMap(Event::getId, Function.identity()));

        dbContext.child(eventsPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                snapshot.getChildren().forEach((e -> {
                    if (eventsToRemove.containsKey(e.getKey()))
                        e.getRef().removeValueAsync();
                }));
            }

            @Override
            public void onCancelled(DatabaseError _) {
            }
        });
    }
}