package com.navimee.tasks;


import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.navimee.tasks.TasksFixedTimes.EVENTS;
import static com.navimee.tasks.TasksFixedTimes.MINUTE;

@Component
public class EventsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    public void executeEventsTask() {
        Logger.LOG(new Log(LogEnum.TASK, "Events update"));

        placesRepository.getAvailableCities().forEach(city -> eventsService.saveFacebookEvents(city.getName()));
    }

    @Scheduled(fixedDelay = EVENTS, initialDelay = MINUTE * 4)
    public void task() {
        this.executeEventsTask();
    }
}
