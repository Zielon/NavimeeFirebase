package com.navimee.tasks.events;


import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.events.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


@Component
public class EventsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    // Once per 1 hour.
    //@Scheduled(cron = "0 0 0/1 * * ?")
    //@Scheduled(fixedRate = 1000 * 60 * 60)
    public void addEventsTask() throws ExecutionException, InterruptedException {

        placesRepository.getAvailableCities().parallelStream().forEach(city -> {
            if (city.getName().equals("SOPOT"))
                Executors.newSingleThreadExecutor().submit(() -> eventsService.saveFacebookEvents(city.getName()));
        });
    }
}
