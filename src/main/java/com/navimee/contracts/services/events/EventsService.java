package com.navimee.contracts.services.events;

import com.navimee.contracts.models.events.Event;

import java.util.List;

public interface EventsService {
    List<Event> getFacebookEvents();
}