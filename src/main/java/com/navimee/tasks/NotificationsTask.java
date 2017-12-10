package com.navimee.tasks;

import com.navimee.contracts.services.NotificationsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class NotificationsTask {

    @Autowired
    NotificationsService notifications;

    // Once per 1 hour.
    //@Scheduled(cron = "0 0 0/1 * * ?")
    public void sendNotification() throws ExecutionException, InterruptedException {
        Logger.LOG(new Log(LogEnum.TASK, "Send notifications"));
        notifications.send();
    }
}
