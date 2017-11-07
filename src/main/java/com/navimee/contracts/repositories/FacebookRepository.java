package com.navimee.contracts.repositories;

import com.navimee.models.entities.Event;
import com.navimee.models.entities.Place;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacebookRepository {

    String eventsPath = "events";
    String placesPath = "places";
    String todayEventsPath = "todayEvents";
    String tomorrowEventsPath = "tomorrowEvents";
    String dayAfterTomorrowEventsPath = "dayAfterTomorrowEvents";
    String historicalEventsPath = "historicalEvents";

    void setEvents(List<Event> events);

    void setPlaces(List<Place> places);

    void updateEvents(List<Event> events, String path);

    void updateHistorical(List<Event> events);

    void removeEvents(List<Event> events, String path);
}
