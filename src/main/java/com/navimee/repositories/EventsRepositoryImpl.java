package com.navimee.repositories;

import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.Add;
import com.navimee.firestore.operations.Get;
import com.navimee.models.entities.events.FbEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.navimee.enums.CollectionEnum.EVENTS;
import static com.navimee.enums.CollectionEnum.SEGREGATED_EVENTS;

@Repository
public class EventsRepositoryImpl implements EventsRepository {

    @Autowired
    Database database;

    @Autowired
    ExecutorService executorService;

    @Autowired
    Add add;

    @Autowired
    Get get;

    @Override
    public List<FbEvent> getEvents(String city) {
        return get.fromCollection(database.getCollection(EVENTS, city), FbEvent.class);
    }

    @Override
    public Future setEvents(List<FbEvent> events, String city) {
        return add.toCollection(database.getCollection(EVENTS, city), events);
    }

    @Override
    public Future sevenDaysSegregation(Map<String, List<FbEvent>> events, String city) {
        return executorService.submit(() ->
                events.forEach((key, segregatedEvents) ->
                        add.toCollection(database.getCollection(SEGREGATED_EVENTS, city).document(key).collection("EVENTS"), segregatedEvents)));
    }

    @Override
    public Future deleteEvents(List<FbEvent> events, String city) {
        return executorService.submit(() -> {
        });
    }

    @Override
    public Future updateHistorical(List<FbEvent> events) {
        return executorService.submit(() -> {
        });
    }
}
