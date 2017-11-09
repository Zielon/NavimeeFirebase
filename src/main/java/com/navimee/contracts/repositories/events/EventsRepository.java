package com.navimee.contracts.repositories.events;

import com.navimee.contracts.models.events.Event;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsRepository {

    String placesPath = "places";
    String todayEventsPath = "todayEvents";
    String tomorrowEventsPath = "tomorrowEvents";
    String dayAfterTomorrowEventsPath = "dayAfterTomorrowEvents";
    String historicalEventsPath = "historicalEvents";

    void updateEvents(List<Event> events, String city);

    void updateHistorical(List<Event> events);

    void removeEvents(List<Event> events, String city);
}
