package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.navimee.tasks.TasksFixedTimes.EVENTS;

@Component
public class FacebookEventsTask {

    @Autowired

    CoordinatesRepository coordinatesRepository;

    @Autowired
    EventsService eventsService;

    public void executeEventsTask() {
        coordinatesRepository.getAvailableCities().thenAcceptAsync(cities -> {
            for (City city : cities) {
                try {
                    eventsService.saveFacebookEvents(city.getName()).join();
                } catch (Exception e) {
                    Logger.LOG(new Log(LogTypes.EXCEPTION, e));
                }
            }
        });
    }

    @Scheduled(fixedDelay = EVENTS)
    public void task() {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executeEventsTask();
    }
}
