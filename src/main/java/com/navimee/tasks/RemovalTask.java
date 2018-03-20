package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.navimee.tasks.TasksFixedTimes.REMOVAL;

@Component
public class RemovalTask {

    @Autowired
    EventsRepository eventsRepository;

    public void executeRemoveEventsTask() {
        eventsRepository.removeEvents();
    }

    @Scheduled(fixedDelay = REMOVAL)
    public void task() {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executeRemoveEventsTask();
    }
}
