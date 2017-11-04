package com.navimee.contracts.repositories;

import com.navimee.models.Event;
import com.navimee.models.Place;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.Future;

@Repository
public interface FacebookRepository {
    void setEvents(List<Event> events);
    void addPlaces(List<Place> places);
    void updateEvents(List<Event> events);
    void updateHistorical(List<Event> events);
    Future<List<Event>> getEvents();
}
