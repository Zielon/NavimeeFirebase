package com.navimee.contracts.services.events;

import com.navimee.contracts.models.events.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventsService {
    List<Event> getFacebookEvents();
}