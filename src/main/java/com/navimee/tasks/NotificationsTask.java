package com.navimee.tasks;

import com.navimee.contracts.services.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class NotificationsTask {

    @Autowired
    Notifications notifications;

    //@Scheduled(fixedRate = 1000 * 60 * 60)
    public void sendNotification() throws ExecutionException, InterruptedException {

    }
}
