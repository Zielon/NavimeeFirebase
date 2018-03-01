package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.navimee.tasks.TasksFixedTimes.REMOVAL;

@Component
public class RemovalTask {

    @Autowired
    EventsRepository eventsRepository;

    public void executeRemoveEventsTask() {
        try {
            eventsRepository.removeEvents().join();
        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }
    }

    @Scheduled(fixedDelay = REMOVAL)
    public void task() {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executeRemoveEventsTask();
    }
}
