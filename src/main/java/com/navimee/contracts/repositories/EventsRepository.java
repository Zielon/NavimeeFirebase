package com.navimee.contracts.repositories;

import com.navimee.models.entities.HotspotEvent;

import java.util.List;
import java.util.concurrent.Future;

public interface EventsRepository {

    // GETTERS
    List<HotspotEvent> getEvents();

    List<HotspotEvent> getEventsBefore(int timeToEnd);

    // SETTERS
    Future setEvents(List<HotspotEvent> events);

    Future removeEvents();
}
