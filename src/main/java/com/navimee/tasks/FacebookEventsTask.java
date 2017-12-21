package com.navimee.tasks;


import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.models.entities.coordinates.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

import static com.navimee.tasks.TasksFixedTimes.EVENTS;

@Component
public class FacebookEventsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    public void executeEventsTask() throws InterruptedException, ExecutionException {
        for (City city : placesRepository.getAvailableCities()) {
            eventsService.saveFacebookEvents(city.getName()).get();
        }
    }

    //@Scheduled(fixedDelay = EVENTS)
    public void task() throws InterruptedException, ExecutionException {
        this.executeEventsTask();
    }
}
