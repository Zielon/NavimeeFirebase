package com.navimee.tasks.events;


import com.navimee.contracts.repositories.FirestoreRepository;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.events.EventsService;
import com.navimee.firestore.Paths;
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

    @Autowired
    FirestoreRepository firestoreRepository;

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
    public void task() throws ExecutionException, InterruptedException {
        this.addEventsTask();
    }
}
