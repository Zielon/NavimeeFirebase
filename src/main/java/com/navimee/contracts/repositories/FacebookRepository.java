package com.navimee.contracts.repositories;

import com.navimee.models.Event;
import com.navimee.models.Place;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacebookRepository {

    String eventsPath = "events";
    String placesPath = "places";
    String historicalEventsPath = "historicalEvents";

    void setEvents(List<Event> events);

    void setPlaces(List<Place> places);

    void updateEvents(List<Event> events);

    void updateHistorical(List<Event> events);

    void removeEvents(List<Event> events);
}
