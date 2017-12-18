package com.navimee.repositories;

import com.navimee.contracts.repositories.NotificationsRepository;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.Notification;
import com.navimee.models.entities.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.navimee.enums.CollectionType.NOTIFICATIONS;

@Repository
public class NotificationsRepositoryImpl implements NotificationsRepository {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    DbGet dbGet;

    @Autowired
    Database database;

    @Override
    public List<Notification> getAvailable() {

        Date warsaw = DateTime.now(DateTimeZone.UTC).plusMinutes(30).toDate();

        List<Notification> uncheckedNotifications =
                dbGet.fromCollection(
                        database.getCollection(NOTIFICATIONS)
                                .whereLessThanOrEqualTo("endTime", warsaw), Notification.class);

        List<Notification> notifications = new ArrayList<>();

        uncheckedNotifications.forEach(notification -> {
            User user = usersRepository.getUser(notification.getUserId());
            if (user.isDayScheduleNotification()) {
                notification.setToken(user.getToken());
                notifications.add(notification);
            }
        });

        return notifications;
    }
}
