package com.navimee.tasks;


import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.navimee.tasks.TasksFixedTimes.EVENTS;

@Component
public class EventsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    @Autowired
    ExecutorService executorService;

    public void executeEventsTask() throws InterruptedException {
        Logger.LOG(new Log(LogEnum.TASK, "Events update"));
        List<Future> futures = new ArrayList<>();

        for (City city : placesRepository.getAvailableCities()) {
            futures.add(eventsService.saveFacebookEvents(city.getName()));
            futures.add(eventsService.savePredictHqEvents(city.getName()));
        }
    }

    @Scheduled(fixedDelay = EVENTS)
    public void task() throws InterruptedException {
        this.executeEventsTask();
    }
}
