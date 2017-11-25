package com.navimee.events.repositories;

import com.google.cloud.firestore.Firestore;
import com.navimee.firestore.Database;
import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.firestore.EntitiesOperations;
import com.navimee.models.entities.events.FbEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.navimee.enums.CollectionEnum.EVENTS;
import static com.navimee.enums.CollectionEnum.SEGREGATED_EVENTS;

@Repository
public class EventsRepositoryImpl implements EventsRepository {

    @Autowired
    Firestore db;

    @Autowired
    Database factory;

    @Override
    public List<FbEvent> getEvents(String city) {
        return EntitiesOperations.getFromCollection(factory.getCollection(EVENTS, city), FbEvent.class);
    }

    @Override
    public Future setEvents(List<FbEvent> events, String city) {
        return EntitiesOperations.addToCollection(factory.getCollection(EVENTS, city), events);
    }

    @Override
    public Future sevenDaysSegregation(Map<String, List<FbEvent>> events, String city) {
        return Executors.newSingleThreadExecutor().submit(() ->
                events.forEach((key, segregatedEvents) ->
                        EntitiesOperations.addToCollection(factory.getCollection(SEGREGATED_EVENTS, city).document(key).collection("EVENTS"), segregatedEvents)));
    }

    @Override
    public Future deleteEvents(List<FbEvent> events, String city) {
        return Executors.newSingleThreadExecutor().submit(() -> {
        });
    }

    @Override
    public Future updateHistorical(List<FbEvent> events) {
        return Executors.newSingleThreadExecutor().submit(() -> {
        });
    }
}
