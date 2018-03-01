package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.navimee.contracts.repositories.NotificationsRepository;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.firestore.PathBuilder;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.Notification;
import com.navimee.models.entities.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.navimee.firestore.FirebasePaths.EVENTS_NOTIFICATION;
import static com.navimee.firestore.FirebasePaths.NOTIFICATIONS;
import static com.navimee.reflection.Utils.nameof;
import static java.util.stream.Collectors.toList;

@Repository
public class NotificationsRepositoryImpl implements NotificationsRepository {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    DbGet dbGet;

    @Autowired
    Firestore database;

    @Autowired
    ExecutorService executorService;

    @Override
    public CompletableFuture<List<Notification>> getAvailableNotifications() {
        return CompletableFuture.supplyAsync(() -> {
            DateTime utc = DateTime.now(DateTimeZone.UTC);
            List<Notification> notifications = null;
            try {
                String isSent = nameof(Notification.class, "sent");
                String endTime = nameof(Notification.class, "endTime");
                String path = new PathBuilder().add(NOTIFICATIONS).addCountry().add(EVENTS_NOTIFICATION).build();

                Query query = database.collection(path)
                        .whereGreaterThanOrEqualTo(endTime, utc.toDate())
                        .whereLessThanOrEqualTo(endTime, utc.plusMinutes(45).toDate())
                        .whereEqualTo(isSent, false);

                notifications = dbGet.fromQuery(query, Notification.class).join();

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            return notifications.stream().filter(notification -> {
                User user = usersRepository.getUser(notification.getUserId()).join();
                if (user.getToken() != null && user.isDayScheduleNotification()) {
                    notification.setToken(user.getToken());
                    return true;
                }
                return false;
            }).collect(toList());
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Notification>> getBigEventsNotifications() {
        throw new NotImplementedException();
    }
}
