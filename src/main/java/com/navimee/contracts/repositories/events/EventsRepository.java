package com.navimee.contracts.repositories.events;

import com.navimee.contracts.models.events.Event;

import java.util.List;
import java.util.concurrent.Future;

public interface EventsRepository {

    String eventsPath = "events";
    String todayEventsPath = "todayEvents";
    String tomorrowEventsPath = "tomorrowEvents";
    String dayAfterTomorrowEventsPath = "dayAfterTomorrowEvents";
    String historicalEventsPath = "historicalEvents";

    Future updateEvents(List<Event> events, String city);

    Future updateHistorical(List<Event> events);

    Future removeEvents(List<Event> events, String city);
}
