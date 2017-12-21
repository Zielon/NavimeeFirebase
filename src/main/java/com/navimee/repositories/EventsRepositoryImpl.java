package com.navimee.repositories;

import com.google.cloud.firestore.Query;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.enums.HotspotType;
import com.navimee.firestore.Database;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.navimee.enums.CollectionType.HOTSPOT;
import static com.navimee.enums.CollectionType.NOTIFICATIONS;

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
    public List<Event> getEvents() {
        return dbGet.fromCollection(
                database.getCollection(HOTSPOT)
                        .whereEqualTo("hotspotType", HotspotType.EVENT), Event.class);
    }

    @Override
    public List<Event> getEventsBefore(int minutesBeforeEnd) {
        DateTime warsaw = LocalDateTime.now(DateTimeZone.UTC).toDateTime();

        return dbGet.fromCollection(
                database.getCollection(HOTSPOT)
                        .whereEqualTo("hotspotType", HotspotType.EVENT)
                        .whereGreaterThanOrEqualTo("endTime", warsaw.toDate())
                        .whereLessThanOrEqualTo("endTime", warsaw.plusMinutes(minutesBeforeEnd).toDate())
                , Event.class);
    }

    @Override
    public Future setEvents(List<Event> events, String city) {
        return dbAdd.toCollection(database.getCollection(HOTSPOT), events, city);
    }

    @Override
    public Future removeEvents() {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogTypes.DELETION, "Delete old events"));

            Date warsaw = LocalDateTime.now(DateTimeZone.UTC).toDate();

            Query hotspot = database.getCollection(HOTSPOT).whereLessThan("endTime", warsaw);
            Query notification = database.getCollection(NOTIFICATIONS).whereLessThan("endTime", warsaw);

            delete.document(hotspot);
            delete.document(notification);

            firebaseRepository.deleteEvents(dbGet.fromCollection(hotspot, Event.class));
        });
    }
}