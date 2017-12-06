package com.navimee.tasks.events;


import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.EventsService;
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

        Logger.LOG(new Log(LogEnum.TASK, "Events update"));

        // placesRepository.deleteCollection(Paths.EVENTS_COLLECTION).get();
        // placesRepository.deleteCollection(Paths.HOTSPOT).get();

        placesRepository.getAvailableCities().forEach(city -> {
            eventsService.saveFacebookEvents(city.getName());
        });
    }

    // Once per 30 minutes.
    @Scheduled(cron = "0 0/30 * * * ?")
    public void task() {
        try {
            this.addEventsTask();
        } catch (Exception e) {
            Logger.LOG(new Log(LogEnum.EXCEPTION, e));
        }
    }
}
