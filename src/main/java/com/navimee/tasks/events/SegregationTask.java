package com.navimee.tasks.events;

import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.events.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class SegregationTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    @Autowired
    EventsRepository eventsRepository;

    // Once per 1 hour.
    // @Scheduled(cron = "0 0 0/1 * * ?")
    public void addSegregationTask() throws ExecutionException, InterruptedException {
        placesRepository.getAvailableCities()
                .parallelStream()
                .forEach(city -> eventsService.saveSevenDaysSegregation(city.getName()));
    }
}
