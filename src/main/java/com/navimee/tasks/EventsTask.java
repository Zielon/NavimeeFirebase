package com.navimee.tasks;


import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.models.entities.coordinates.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class EventsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    public void executeEventsTask() throws InterruptedException, ExecutionException {

        for (City city : placesRepository.getAvailableCities()) {
            eventsService.saveFacebookEvents(city.getName()).get();
            //eventsService.savePredictHqEvents(city.getName()).get();
        }
    }

    //@Scheduled(fixedDelay = EVENTS)
    public void task() throws InterruptedException, ExecutionException {
        this.executeEventsTask();
    }
}
