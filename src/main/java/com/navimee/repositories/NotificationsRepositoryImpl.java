package com.navimee.repositories;

import com.google.cloud.firestore.Query;
import com.navimee.contracts.repositories.NotificationsRepository;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.Event;
import com.navimee.models.entities.Notification;
import com.navimee.models.entities.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.navimee.enums.CollectionType.HOTSPOT;
import static com.navimee.enums.CollectionType.NOTIFICATIONS;
import static java.util.stream.Collectors.toList;

@Repository
public class NotificationsRepositoryImpl implements NotificationsRepository {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    DbGet dbGet;

    @Autowired
    Database database;

    @Override
    public List<Notification> getAvailableNotifications() {

        DateTime warsaw = DateTime.now(DateTimeZone.UTC);

        Query query = database.getCollection(NOTIFICATIONS)
                .whereGreaterThanOrEqualTo("endTime", warsaw.toDate())
                .whereLessThanOrEqualTo("endTime", warsaw.plusMinutes(45).toDate())
                .whereEqualTo("isSent", false);

        List<Notification> uncheckedNotifications = dbGet.fromCollection(query, Notification.class);
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

    @Override
    public List<Notification> getBigEventsNotifications() {

        DateTime warsaw = DateTime.now(DateTimeZone.UTC);

        Query query = database.getCollection(HOTSPOT)
                .whereGreaterThanOrEqualTo("endTime", warsaw.toDate())
                .whereLessThanOrEqualTo("endTime", warsaw.plusMinutes(45).toDate());

        List<Event> bigEvents = dbGet.fromCollection(query, Event.class).stream()
                .filter(event -> event.getRank() >= 80).collect(toList());

        return usersRepository.getUsersWithBigEventsOn().stream()
                .map(user -> bigEvents.stream()
                        .map(event -> {
                            Notification notification = new Notification();

                            notification.setToken(user.getToken());
                            notification.setGeoPoint(event.getGeoPoint());
                            notification.setEndTime(event.getEndTime());
                            notification.setHotspotType(event.getHotspotType().toString());
                            notification.setUserId(user.getId());
                            notification.setTitle(event.getTitle());

                            return notification;
                        }).collect(toList())).flatMap(List::stream).collect(toList());
    }
}
