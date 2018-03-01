package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.places.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.models.entities.places.facebook.FbPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PredictHqEventsTasks {
    @Autowired
    PlacesRepository<FbPlace> placesRepository;

    @Autowired
    EventsService eventsService;

    public void executeEventsTask() {
        /*for (City city : placesRepository.getAvailableCities()) {
            try {
                eventsService.savePredictHqEvents(city.getName()).get();
            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            }
        }*/
    }

    //@Scheduled(fixedDelay = EVENTS)
    public void task() {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executeEventsTask();
    }
}
