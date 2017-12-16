package com.navimee.tasks;

import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import static com.navimee.tasks.TasksFixedTimes.REMOVAL;

@Component
public class RemovalTask {

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    ExecutorService executorService;

    synchronized public void executeRemoveEventsTask() throws InterruptedException {
        Logger.LOG(new Log(LogEnum.DELETION, "Delete old events"));
        eventsRepository.removeEvents();
    }

    @Scheduled(fixedDelay = REMOVAL)
    public void task() throws InterruptedException {
        this.executeRemoveEventsTask();
    }
}
