package com.navimee.tasks.events;


import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.events.EventsService;
import com.navimee.logger.Log;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class EventsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    // Once per 1 hour.
    //@Scheduled(fixedRate = 1000 * 60 * 60)
    //@Scheduled(cron = "0 0 0/1 * * ?")
    public void addEventsTask() throws ExecutionException, InterruptedException {

        Logger.LOG(new Log(LogEnum.TASK, "Events update", 0));

        //placesRepository.deleteCollection(EVENTS_COLLECTION).get();

        placesRepository.getAvailableCities().forEach(city -> {
            //if (city.getName().equals("GDANSK"))
            eventsService.saveFacebookEvents(city.getName());
        });
    }
}
