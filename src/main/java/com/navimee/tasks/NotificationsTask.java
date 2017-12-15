package com.navimee.tasks;

import com.navimee.contracts.services.NotificationsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.navimee.tasks.TasksFixedTimes.MINUTE;
import static com.navimee.tasks.TasksFixedTimes.NOTIFICATIONS;

@Component
public class NotificationsTask {

    @Autowired
    NotificationsService notifications;

    public void executeSendNotification() {
        Logger.LOG(new Log(LogEnum.TASK, "Send notifications"));

        notifications.send();
    }

    @Scheduled(fixedDelay = NOTIFICATIONS, initialDelay = MINUTE * 2)
    public void task() {
        this.executeSendNotification();
    }
}
