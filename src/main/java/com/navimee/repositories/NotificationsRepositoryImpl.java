package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.NotificationsRepository;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.Notification;
import com.navimee.models.entities.User;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.navimee.firestore.Paths.NOTIFICATIONS_COLLECTION;

public class NotificationsRepositoryImpl implements NotificationsRepository {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    DbGet dbGet;

    @Autowired
    Firestore db;

    @Override
    public List<Notification> getAvailable() {
        Date warsaw = LocalDateTime.now(DateTimeZone.forID("Europe/Warsaw")).plusMinutes(30).toDate();

        List<Notification> uncheckedNotifications = dbGet.fromCollection(
                db.collection(NOTIFICATIONS_COLLECTION)
                        .whereLessThanOrEqualTo("endTime", warsaw), Notification.class);

        List<Notification> notifications = new ArrayList<>();

        uncheckedNotifications.forEach(notification -> {
            User user = usersRepository.getUser(notification.getUserId());
            if(user.isDayScheduleNotification())
                notifications.add(notification);
        });

        return notifications;
    }
}
