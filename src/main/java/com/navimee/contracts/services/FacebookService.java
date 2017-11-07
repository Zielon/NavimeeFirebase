package com.navimee.contracts.services;

import com.navimee.models.entities.Event;
import com.navimee.models.entities.Place;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FacebookService {
    List<Event> getEvents();

    List<Place> getPlaces();
}
