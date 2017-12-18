package com.navimee.tasks;

import com.navimee.contracts.repositories.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Component
public class RemovalTask {

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    ExecutorService executorService;

    synchronized public void executeRemoveEventsTask() throws InterruptedException, ExecutionException {
        eventsRepository.removeEvents().get();
    }

    //@Scheduled(fixedDelay = REMOVAL)
    public void task() throws InterruptedException, ExecutionException {
        this.executeRemoveEventsTask();
    }
}
