package com.navimee.events.repositories;

import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.contracts.models.events.Event;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventsRepositoryImpl implements EventsRepository {


    @Override
    public void updateEvents(List<Event> events, String city) {

    }

    @Override
    public void updateHistorical(List<Event> events) {

    }

    @Override
    public void removeEvents(List<Event> events, String city) {

    }
}
