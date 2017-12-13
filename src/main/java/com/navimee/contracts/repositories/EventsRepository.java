package com.navimee.contracts.repositories;

import com.navimee.models.entities.events.FbEvent;

import java.util.List;
import java.util.concurrent.Future;

public interface EventsRepository {

    // GETTERS
    List<FbEvent> getEvents();

    List<FbEvent> getEventsBeforeEnd(int timeToEnd);

    // SETTERS
    Future setEvents(List<FbEvent> events);

    Future updateHistorical(List<FbEvent> events);

    Future deleteEvents(List<FbEvent> events);

    Future removeOldEvents();
}
