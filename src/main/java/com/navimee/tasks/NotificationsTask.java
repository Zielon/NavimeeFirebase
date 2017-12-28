package com.navimee.tasks;

import com.navimee.contracts.services.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

import static com.navimee.tasks.TasksFixedTimes.NOTIFICATIONS;

@Component
public class NotificationsTask {

    @Autowired
    NotificationsService notifications;

    public void executeSendNotification() throws InterruptedException, ExecutionException {
        notifications.sendDaySchedule().get();
        notifications.sendBigEvents().get();
    }

    @Scheduled(fixedDelay = NOTIFICATIONS)
    public void task() throws InterruptedException, ExecutionException {
        executeSendNotification();
    }
}
