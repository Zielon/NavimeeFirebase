package com.navimee.repositories;

import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.enums.HotspotType;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.Add;
import com.navimee.firestore.operations.Get;
import com.navimee.models.entities.events.FbEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
    public List<FbEvent> getEvents() {
        return get.fromCollection(database.getHotspot().whereEqualTo("hotspotType", HotspotType.FACEBOOK_EVENT), FbEvent.class);
    }

    @Override
    public Future setEvents(List<FbEvent> events) {
        return add.toCollection(database.getHotspot(), events);
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
