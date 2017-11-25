package com.navimee.contracts.repositories.events;

import com.navimee.models.entities.events.FbEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface EventsRepository {

    // GETTERS
    List<FbEvent> getEvents(String city);

    // SETTERS
    Future setEvents(List<FbEvent> events, String city);

    Future sevenDaysSegregation(Map<String, List<FbEvent>> events, String city);

    Future updateHistorical(List<FbEvent> events);

    Future deleteEvents(List<FbEvent> events, String city);
}
