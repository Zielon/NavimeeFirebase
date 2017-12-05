package com.navimee.tasks.events;


import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.events.EventsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class EventsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    public void addEventsTask() throws ExecutionException, InterruptedException {

        Logger.LOG(new Log(LogEnum.TASK, "Events update", 0));

        //placesRepository.deleteCollection(EVENTS_COLLECTION).get();

        placesRepository.getAvailableCities().forEach(city -> {
            //if (city.getName().equals("GDANSK"))
            eventsService.saveFacebookEvents(city.getName());
        });
    }

    // Once per 1 hour.
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void task() throws ExecutionException, InterruptedException {
        this.addEventsTask();
    }
}
