package com.navimee.tasks;

import com.navimee.contracts.services.NotificationsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

import static com.navimee.tasks.TasksFixedTimes.NOTIFICATIONS;

@Component
public class NotificationsTask {

    @Autowired
    NotificationsService notifications;

    public void executeSendNotification() throws ExecutionException, InterruptedException {
        Logger.LOG(new Log(LogEnum.TASK, "Send notifications"));

        notifications.send();
    }

    @Scheduled(fixedDelay = NOTIFICATIONS)
    public void task() {
        try {
            this.executeSendNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
