package com.navimee.contracts.repositories;

import com.navimee.models.Event;

public interface FacebookRepository {
    void addEvent(Event event);
    void updateEvent(Event event);
}
