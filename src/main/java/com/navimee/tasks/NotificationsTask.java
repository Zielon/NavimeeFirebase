package com.navimee.tasks;

import com.navimee.contracts.services.NotificationsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationsTask {

    @Autowired
    NotificationsService notifications;

    public void addSendNotification() {
        Logger.LOG(new Log(LogEnum.TASK, "Send notifications"));

        notifications.send();
    }

    @Scheduled(cron = "0 0/5 0 * * ?")
    public void sendNotificationTask() {
        this.addSendNotification();
    }
}
