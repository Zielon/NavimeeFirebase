package com.navimee.contracts.services;

import com.navimee.models.Event;
import com.navimee.models.Place;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FacebookService {
    List<Event> getEvents();
    List<Place> getPlaces();
}
