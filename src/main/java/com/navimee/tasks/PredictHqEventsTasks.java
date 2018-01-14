package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PredictHqEventsTasks {
    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    public void executeEventsTask() {
        for (City city : placesRepository.getAvailableCities()) {
            try {
                eventsService.savePredictHqEvents(city.getName()).get();
            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            }
        }
    }

    //@Scheduled(fixedDelay = EVENTS)
    public void task() {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executeEventsTask();
    }
}
