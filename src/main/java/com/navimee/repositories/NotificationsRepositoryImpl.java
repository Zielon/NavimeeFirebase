package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.navimee.contracts.repositories.NotificationsRepository;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.Notification;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.navimee.asyncCollectors.CompletionCollector.sequence;
import static com.navimee.firestore.FirebasePaths.NOTIFICATIONS;
import static java.util.stream.Collectors.toList;

@Repository
public class NotificationsRepositoryImpl implements NotificationsRepository {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    DbGet dbGet;

    @Autowired
    Firestore database;

    @Override
    public CompletableFuture<List<Notification>> getAvailableNotifications() {

        DateTime utc = DateTime.now(DateTimeZone.UTC);

        Query query = database.collection(NOTIFICATIONS)
                .whereGreaterThanOrEqualTo("endTime", utc.toDate())
                .whereLessThanOrEqualTo("endTime", utc.plusMinutes(45).toDate())
                .whereEqualTo("isSent", false);

        List<CompletableFuture<Notification>> notifications = dbGet.fromQuery(query, Notification.class)
                .thenApplyAsync(uncheckedNotifications ->
                        uncheckedNotifications.stream().map(
                                notification -> usersRepository.getUser(notification.getUserId())
                                        .thenApplyAsync(user -> {
                                            if (user.getToken() != null && user.isDayScheduleNotification()) {
                                                notification.setToken(user.getToken());
                                                return notification;
                                            }
                                            return null;
                                        })).collect(toList())
                ).join();

        return sequence(notifications);
    }

    @Override
    public CompletableFuture<List<Notification>> getBigEventsNotifications() {
        throw new NotImplementedException();
    }
}
