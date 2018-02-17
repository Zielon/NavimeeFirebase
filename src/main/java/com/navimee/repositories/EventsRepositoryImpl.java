package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.enums.HotspotType;
import com.navimee.firestore.operations.DbAdd;
import com.navimee.firestore.operations.DbDelete;
import com.navimee.firestore.operations.DbGet;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Event;
import com.navimee.models.entities.Log;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.navimee.firestore.FirebasePaths.HOTSPOT;

@Repository
public class EventsRepositoryImpl implements EventsRepository {

    @Autowired
    Firestore database;

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
    public CompletableFuture<List<Event>> getEvents() {
        return dbGet.fromQuery(
                database.collection(HOTSPOT)
                        .whereEqualTo("hotspotType", HotspotType.EVENT), Event.class);
    }

    @Override
    public CompletableFuture<List<Event>> getEventsBefore(int minutesBeforeEnd) {
        DateTime warsaw = LocalDateTime.now(DateTimeZone.UTC).toDateTime();

        return dbGet.fromQuery(
                database.collection(HOTSPOT)
                        .whereEqualTo("hotspotType", HotspotType.EVENT)
                        .whereGreaterThanOrEqualTo("endTime", warsaw.toDate())
                        .whereLessThanOrEqualTo("endTime", warsaw.plusMinutes(minutesBeforeEnd).toDate()), Event.class);
    }

    @Override
    public CompletableFuture<Void> setEvents(List<Event> events, String city) {
        return dbAdd.toCollection(database.collection(HOTSPOT), events);
    }

    @Override
    public CompletableFuture<Void> removeEvents() {
        return CompletableFuture.runAsync(() -> {
            Logger.LOG(new Log(LogTypes.DELETION, "Delete old events"));

            Date warsaw = LocalDateTime.now(DateTimeZone.UTC).toDate();

            Query hotspot = database.collection(HOTSPOT).whereLessThan("endTime", warsaw);
            Query notification = database.collection(HOTSPOT).whereLessThan("endTime", warsaw);

            delete.document(hotspot);
            delete.document(notification);

            dbGet.fromQuery(hotspot, Event.class).thenAcceptAsync(events -> firebaseRepository.deleteEvents(events));

        }, executorService);
    }
}