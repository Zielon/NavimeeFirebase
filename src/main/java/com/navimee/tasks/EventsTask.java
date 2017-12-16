package com.navimee.tasks;


import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.navimee.asyncCollectors.CompletionCollector.waitForFutures;
import static com.navimee.tasks.TasksFixedTimes.EVENTS;
import static com.navimee.tasks.TasksFixedTimes.MINUTE;

@Component
public class EventsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    @Autowired
    ExecutorService executorService;

    public void executeEventsTask() {
        Logger.LOG(new Log(LogEnum.TASK, "Events update"));
        List<Future> futures = new ArrayList<>();

        placesRepository.getAvailableCities().forEach(city -> futures.add(eventsService.saveFacebookEvents(city.getName())));
    }

    @Scheduled(fixedDelay = EVENTS)
    public void task() {
        this.executeEventsTask();
    }
}
