package com.navimee.events.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.contracts.models.events.Event;
import com.navimee.contracts.models.pojos.EventPojo;
import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.events.Events;
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

    private Firestore db = FirebaseInitialization.getDatabaseReference();

    @Override
    public List<Event> getEvents(String city) {

        List<Event> events = new ArrayList<>();
        ApiFuture<DocumentSnapshot> documentSnapshot = db.collection(eventsPath).document(city).get();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());

        try {
            documentSnapshot.get().getData().forEach((k, v) -> events.add(mapper.convertValue(v, Event.class)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return events;
    }

    @Override
    public Future updateEvents(List<Event> events, String city) {

        return Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Map<String, EventPojo> pojos = events.stream().map(Event::toPojo).collect(Collectors.toMap(pojo -> pojo.id, Function.identity()));
                db.collection(eventsPath).document(city).set(pojos, SetOptions.merge()).get();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
            System.out.println("EVENTS ADDED " + events.size() + " " + city + " at " + new Date());
        });
    }

    @Override
    public Future sevenDaysSegregation(List<Event> events, String city) {

        return Executors.newSingleThreadExecutor().submit(() -> {
            Map<String, List<Event>> sevenDaysSegregation = Events.sevenDaysSegregation(events);

            sevenDaysSegregation.forEach((key, segregatedEvents) -> {
                Map<String, EventPojo> pojos = segregatedEvents.stream().map(Event::toPojo).collect(Collectors.toMap(pojo -> pojo.id, Function.identity()));
                try {
                    db.collection(segregatetEventsPath).document(city).collection(key).document("events").set(pojos).get();
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
