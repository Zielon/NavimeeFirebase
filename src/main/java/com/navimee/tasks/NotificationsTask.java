package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.services.NotificationsService;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.navimee.tasks.TasksFixedTimes.NOTIFICATIONS;

@Component
public class NotificationsTask {

    @Autowired
    NotificationsService notifications;

    public void executeSendNotification() {
        try {
            notifications.sendDaySchedule();
        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }
    }

    @Scheduled(fixedDelay = NOTIFICATIONS)
    public void task() {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        executeSendNotification();
    }
}
