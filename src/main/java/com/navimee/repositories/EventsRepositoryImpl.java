package com.navimee.repositories;

import com.google.cloud.firestore.Query;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.enums.HotspotType;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.DbAdd;
import com.navimee.firestore.operations.DbDelete;
import com.navimee.firestore.operations.DbGet;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.HotspotEvent;
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
    public List<HotspotEvent> getEvents() {
        return dbGet.fromCollection(database.getHotspot().whereEqualTo("hotspotType", HotspotType.EVENT), HotspotEvent.class);
    }

    @Override
    public List<HotspotEvent> getEventsBefore(int timeToEnd) {
        DateTime warsaw = LocalDateTime.now(DateTimeZone.forID("Europe/Warsaw")).toDateTime();
        return dbGet.fromCollection(
                database.getHotspot()
                        .whereEqualTo("hotspotType", HotspotType.EVENT)
                        .whereGreaterThanOrEqualTo("endTime", warsaw.toDate())
                        .whereLessThanOrEqualTo("endTime", warsaw.plusMinutes(timeToEnd).toDate())
                , HotspotEvent.class);
    }

    @Override
    public Future setEvents(List<HotspotEvent> events) {
        return dbAdd.toCollection(database.getHotspot(), events);
    }

    @Override
    public Future removeEvents() {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogEnum.DELETION, "Delete old events"));

            Date warsaw = LocalDateTime.now(DateTimeZone.forID("Europe/Warsaw")).toDate();
            Query query = database.getHotspot().whereLessThan("endTime", warsaw);
            delete.document(query);
            firebaseRepository.deleteEvents(dbGet.fromCollection(query, HotspotEvent.class));
        });
    }
}