package com.navimee.tasks.events;

import com.navimee.contracts.repositories.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RemoveEventsTask {

    @Autowired
    EventsRepository eventsRepository;

    public void addRemoveEventsTask() {
        eventsRepository.removeOldEvents();
    }

    // Once per 1 hour.
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void task() {
        this.addRemoveEventsTask();
    }
}
