package com.navimee.contracts.repositories;

import com.navimee.models.entities.Event;

import java.util.List;
import java.util.concurrent.Future;

public interface EventsRepository {

    // GETTERS
    List<Event> getEvents();

    List<Event> getEventsBefore(int timeToEnd);

    // SETTERS
    Future setEvents(List<Event> events, String city);

    Future removeEvents();
}
