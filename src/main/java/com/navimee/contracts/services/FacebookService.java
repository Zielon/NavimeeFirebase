package com.navimee.contracts.services;

import com.navimee.models.Event;
import com.navimee.models.Place;

import java.util.List;

public interface FacebookService {
    List<Event> getEvents();
    List<Place> getPlaces();
}
