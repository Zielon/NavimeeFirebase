package com.navimee.contracts.repositories;

import com.navimee.models.Event;
import com.navimee.models.Place;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacebookRepository {
    void addEvents(List<Event> events);
    void addPlaces(List<Place> places);
    void updateEvents(List<Event> events);
}
