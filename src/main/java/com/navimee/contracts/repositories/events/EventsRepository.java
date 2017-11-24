package com.navimee.contracts.repositories.events;

import com.navimee.models.entities.events.FbEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface EventsRepository {

    // FIRESTORE PATHS
    String eventsPath = "events";
    String segregatetEventsPath = "segregatedEvents";

    List<FbEvent> getEvents(String city);

    Future setEvents(List<FbEvent> events, String city);

    Future updateHistorical(List<FbEvent> events);

    Future sevenDaysSegregation(Map<String, List<FbEvent>> events, String city);

    Future deleteEvents(List<FbEvent> events, String city);
}
