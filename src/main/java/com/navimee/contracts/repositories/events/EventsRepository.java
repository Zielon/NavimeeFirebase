package com.navimee.contracts.repositories.events;

import com.navimee.contracts.models.events.Event;

import java.util.List;
import java.util.concurrent.Future;

public interface EventsRepository {

    String eventsPath = "events";
    String segregatetEventsPath = "segregatedEvents";

    List<Event> getEvents(String city);

    Future updateEvents(List<Event> events, String city);

    Future sevenDaysSegregation(List<Event> events, String city);

    Future updateHistorical(List<Event> events);

    Future removeEvents(List<Event> events, String city);
}
