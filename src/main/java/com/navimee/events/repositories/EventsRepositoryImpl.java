package com.navimee.events.repositories;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.contracts.models.events.Event;
import com.navimee.contracts.repositories.events.EventsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class EventsRepositoryImpl implements EventsRepository {

    private Firestore db = FirebaseInitialization.getDatabaseReference();

    @Override
    public Future updateEvents(List<Event> events, String city) {

        return Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Map<String, Event> e = events.stream().collect(Collectors.toMap(Event::getId, Function.identity()));
                db.collection(eventsPath).document(city).set(e, SetOptions.merge()).get();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
        });
    }

    @Override
    public Future updateHistorical(List<Event> events) {
        return Executors.newSingleThreadExecutor().submit(() -> {
        });
    }

    @Override
    public Future removeEvents(List<Event> events, String city) {
        return Executors.newSingleThreadExecutor().submit(() -> {
        });
    }
}
