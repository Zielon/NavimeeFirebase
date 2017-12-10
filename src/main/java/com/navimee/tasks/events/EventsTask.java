package com.navimee.tasks.events;


import com.navimee.contracts.repositories.FirestoreRepository;
import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    @Autowired
    FirestoreRepository firestoreRepository;

    public void addEventsTask() {

        Logger.LOG(new Log(LogEnum.TASK, "Events update"));

        // firestoreRepository.deleteCollection(Paths.EVENTS_COLLECTION);
        // placesRepository.deleteCollection(Paths.HOTSPOT).get();

        placesRepository.getAvailableCities().forEach(city -> {
            eventsService.saveFacebookEvents(city.getName());
        });
    }

    // Once per 30 minutes.
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void task() {
        this.addEventsTask();
    }
}
