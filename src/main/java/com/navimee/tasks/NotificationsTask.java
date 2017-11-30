package com.navimee.tasks;

import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.contracts.services.NotificationsService;
import com.navimee.logger.Log;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class NotificationsTask {

    @Autowired
    NotificationsService notifications;

    @Autowired
    UsersRepository usersRepository;

    //@Scheduled(fixedRate = 1000 * 60 * 60)
    public void sendNotification() throws ExecutionException, InterruptedException {
        Logger.LOG(new Log(LogEnum.TASK, "Send notifications", 0));
        List<User> users = usersRepository.getUsers();
        notifications.send(users);
    }
}