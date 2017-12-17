package com.navimee.services;

import com.google.cloud.firestore.Firestore;
import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.repositories.NotificationsRepository;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.contracts.services.NotificationsService;
import com.navimee.firestore.operations.DbGet;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.bo.FbEvent;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.Notification;
import com.navimee.models.entities.User;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.client.settings.PropertiesBasedSettings;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.requests.notification.NotificationPayload;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.navimee.firestore.Paths.NOTIFICATIONS_COLLECTION;

@Service
public class NotificationsServiceImpl implements NotificationsService {

    @Autowired
    ExecutorService executorService;

    @Autowired
    GoogleConfiguration googleConfiguration;

    @Autowired
    NotificationsRepository notificationsRepository;

    @Override
    public Future send() {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogEnum.TASK, "Send notifications"));

            List<Notification> notifications = notificationsRepository.getAvailable();
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM HH:mm:ss");

            Properties properties = new Properties();
            properties.setProperty("fcm.api.url", googleConfiguration.fmcApiUrl);
            properties.setProperty("fcm.api.key", googleConfiguration.fmcApiKey);

            try {
                try (FcmClient client = new FcmClient(PropertiesBasedSettings.createFromProperties(properties))) {

                    FcmMessageOptions options = FcmMessageOptions.builder().setTimeToLive(Duration.ofHours(1)).build();

                    notifications.forEach(notification -> {
                        DateTime endTime = new DateTime(notification.getEndTime());
                        NotificationPayload payload =
                                NotificationPayload.builder()
                                        .setBody(notification.getTitle())
                                        .setTitle("Event")
                                        .setTag("The event ends at " + dtf.print(endTime))
                                        .build();

                        Map<String, FbEvent> data = new HashMap<>();

                        DataUnicastMessage message = new DataUnicastMessage(options, notification.getToken(), data, payload);
                        client.send(message).getResults();
                    });
                }
            } catch (Exception e) {
                Logger.LOG(new Log(LogEnum.EXCEPTION, e));
            }
        });
    }
}
