package com.navimee.repositories;

import com.google.cloud.firestore.Query;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.enums.HotspotType;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.DbAdd;
import com.navimee.firestore.operations.DbDelete;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.events.Event;
import com.navimee.models.entities.events.FbEvent;
import com.navimee.models.entities.events.PhqEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
    FirebaseRepository firebaseRepository;

    @Autowired
    DbAdd dbAdd;

    @Autowired
    DbGet dbGet;

    @Autowired
    DbDelete delete;

    @Override
    public List<FbEvent> getFacebookEvents() {
        return dbGet.fromCollection(database.getHotspot().whereEqualTo("hotspotType", HotspotType.FACEBOOK_EVENT), FbEvent.class);
    }

    @Override
    public List<PhqEvent> getPredictHqEvents() {
        return dbGet.fromCollection(database.getHotspot().whereEqualTo("hotspotType", HotspotType.PREDICTHQ_EVENT), PhqEvent.class);
    }

    @Override
    public List<FbEvent> getEventsBefore(int timeToEnd) {
        DateTime warsaw = LocalDateTime.now(DateTimeZone.forID("Europe/Warsaw")).toDateTime();
        return dbGet.fromCollection(
                database.getHotspot()
                        .whereEqualTo("hotspotType", HotspotType.FACEBOOK_EVENT)
                        .whereGreaterThanOrEqualTo("endTime", warsaw.toDate())
                        .whereLessThanOrEqualTo("endTime", warsaw.plusMinutes(timeToEnd).toDate())
                , FbEvent.class);
    }

    @Override
    public Future setFacebookEvents(List<FbEvent> events) {
        return dbAdd.toCollection(database.getHotspot(), events);
    }

    @Override
    public Future setPredictHqEvents(List<PhqEvent> events) {
        return dbAdd.toCollection(database.getHotspot(), events);
    }

    @Override
    public Future removeEvents() {
        return executorService.submit(() -> {
            Date warsaw = LocalDateTime.now(DateTimeZone.forID("Europe/Warsaw")).toDate();
            Query query = database.getHotspot().whereLessThan("endTime", warsaw);
            delete.document(query);
            firebaseRepository.deleteEvents(dbGet.fromCollection(query, FbEvent.class));
        });
    }
}