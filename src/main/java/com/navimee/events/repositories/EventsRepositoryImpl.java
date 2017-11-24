package com.navimee.events.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.models.entities.events.FbEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class EventsRepositoryImpl implements EventsRepository {

    @Autowired
    Firestore db;

    @Override
    public List<FbEvent> getEvents(String city) {

        List<FbEvent> events = new ArrayList<>();
        ApiFuture<DocumentSnapshot> documentSnapshot = db.collection(eventsPath).document(city).get();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());

        try {
            documentSnapshot.get().getData().forEach((k, v) -> events.add(mapper.convertValue(v, FbEvent.class)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return events;
    }

    @Override
    public Future setEvents(List<FbEvent> events, String city) {

        return Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Map<String, FbEvent> entities = events.stream().collect(Collectors.toMap(pojo -> pojo.getId(), Function.identity()));
                for (Map.Entry<String, FbEvent> entry : entities.entrySet())
                    db.collection(eventsPath).document("byCity").collection(city).document(entry.getKey()).set(entry.getValue()).get();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
            System.out.println("EVENTS ADDED " + events.size() + " " + city + " at " + new Date());
        });
    }

    @Override
    public Future sevenDaysSegregation(Map<String, List<FbEvent>> events, String city) {

        return Executors.newSingleThreadExecutor().submit(() -> {
            events.forEach((key, segregatedEvents) -> {
                Map<String, FbEvent> entities = segregatedEvents.stream().collect(Collectors.toMap(pojo -> pojo.getId(), Function.identity()));
                try {
                    db.collection(segregatetEventsPath).document(city).collection(key).document("events").set(entities).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("SEGREGATED EVENTS ADDED " + city + " at " + new Date());
        });
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
