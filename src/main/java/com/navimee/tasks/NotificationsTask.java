package com.navimee.tasks;

import com.navimee.contracts.services.NotificationsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationsTask {

    @Autowired
    NotificationsService notifications;

    public void executeSendNotification() throws InterruptedException {
        Logger.LOG(new Log(LogEnum.TASK, "Send notifications"));
        notifications.send();
    }

    //@Scheduled(fixedDelay = NOTIFICATIONS)
    public void task() throws InterruptedException {
        this.executeSendNotification();
    }
}
