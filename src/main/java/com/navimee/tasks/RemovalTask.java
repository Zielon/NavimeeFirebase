package com.navimee.tasks;

import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static com.navimee.tasks.TasksFixedTimes.MINUTE;
import static com.navimee.tasks.TasksFixedTimes.REMOVAL;

@Component
public class RemovalTask {

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    ExecutorService executorService;

    public void executeRemoveEventsTask() throws ExecutionException, InterruptedException {
        Logger.LOG(new Log(LogEnum.DELETION, "Delete old events"));

        eventsRepository.removeEvents().get();
    }

    @Scheduled(fixedDelay = REMOVAL, initialDelay = MINUTE * 3)
    public void task() throws ExecutionException, InterruptedException {
        this.executeRemoveEventsTask();
    }
}
