package com.navimee.tasks;

import com.navimee.contracts.repositories.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static com.navimee.tasks.TasksFixedTimes.REMOVAL;

@Component
public class RemovalTask {

    @Autowired
    EventsRepository eventsRepository;

    synchronized public void executeRemoveEventsTask() throws InterruptedException, ExecutionException {
        eventsRepository.removeEvents().get();
    }

    @Scheduled(fixedDelay = REMOVAL)
    public void task() throws InterruptedException, ExecutionException {
        this.executeRemoveEventsTask();
    }
}
