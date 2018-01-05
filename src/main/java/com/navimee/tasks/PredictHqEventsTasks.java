package com.navimee.tasks;

import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

import static com.navimee.tasks.TasksFixedTimes.EVENTS;

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

    @Scheduled(fixedDelay = EVENTS)
    public void task() throws InterruptedException, ExecutionException {
        this.executeEventsTask();
    }
}
