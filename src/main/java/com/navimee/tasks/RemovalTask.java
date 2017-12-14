package com.navimee.tasks;

import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RemovalTask {

    @Autowired
    EventsRepository eventsRepository;

    public void addRemoveEventsTask() {
        Logger.LOG(new Log(LogEnum.DELETION, "Delete old events"));

        eventsRepository.removeEvents();
    }

    @Scheduled(cron = "0 40 0 * * ?")
    public void task() {
        this.addRemoveEventsTask();
    }
}
