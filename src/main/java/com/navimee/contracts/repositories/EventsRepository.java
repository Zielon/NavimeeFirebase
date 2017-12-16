package com.navimee.contracts.repositories;

import com.navimee.models.entities.events.Event;
import com.navimee.models.entities.events.FbEvent;
import com.navimee.models.entities.events.PhqEvent;

import java.util.List;
import java.util.concurrent.Future;

public interface EventsRepository {

    // GETTERS
    List<FbEvent> getFacebookEvents();

    List<PhqEvent> getPredictHqEvents();

    List<FbEvent> getEventsBefore(int timeToEnd);

    // SETTERS
    Future setFacebookEvents(List<FbEvent> events);

    Future setPredictHqEvents(List<PhqEvent> events);

    Future removeEvents();
}
