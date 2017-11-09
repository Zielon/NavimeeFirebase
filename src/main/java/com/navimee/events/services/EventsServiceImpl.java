package com.navimee.events.services;

import com.navimee.contracts.models.events.Event;
import com.navimee.contracts.services.events.EventsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventsServiceImpl implements EventsService {
    @Override
    public List<Event> getFacebookEvents() {
        return null;
    }
}
