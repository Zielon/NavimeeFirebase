package com.navimee.tasks;


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

    public void addEventsTask() {
        Logger.LOG(new Log(LogEnum.TASK, "Events update"));

        placesRepository.getAvailableCities().forEach(city -> eventsService.saveFacebookEvents(city.getName()));
    }

    @Scheduled(cron = "0 0/30 0 * * ?")
    public void task() {
        this.addEventsTask();
    }
}
