package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.services.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.navimee.tasks.TasksFixedTimes.NOTIFICATIONS;

@Component
public class NotificationsTask {

    @Autowired
    NotificationsService notifications;

    public void executeSendNotification() {
        notifications.sendDaySchedule();
    }

    @Scheduled(fixedDelay = NOTIFICATIONS)
    public void task() {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        executeSendNotification();
    }
}
