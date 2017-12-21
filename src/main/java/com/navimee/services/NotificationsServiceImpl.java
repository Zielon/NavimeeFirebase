package com.navimee.services;

import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.repositories.NotificationsRepository;
import com.navimee.contracts.services.NotificationsService;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.DbAdd;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.Notification;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.client.settings.PropertiesBasedSettings;
import de.bytefish.fcmjava.model.enums.PriorityEnum;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.responses.FcmMessageResultItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.navimee.enums.CollectionType.NOTIFICATIONS;

@Service
public class NotificationsServiceImpl implements NotificationsService {

    @Autowired
    ExecutorService executorService;

    @Autowired
    GoogleConfiguration googleConfiguration;

    @Autowired
    NotificationsRepository notificationsRepository;

    @Autowired
    DbAdd dbAdd;

    @Autowired
    Database database;

    @Override
    public Future send() {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogTypes.TASK, "Send notifications"));

            List<Notification> notifications = notificationsRepository.getAvailable();

            Properties properties = new Properties();
            properties.setProperty("fcm.api.url", googleConfiguration.fmcApiUrl);
            properties.setProperty("fcm.api.key", googleConfiguration.fmcApiKey);

            try {
                try (FcmClient client = new FcmClient(PropertiesBasedSettings.createFromProperties(properties))) {

                    FcmMessageOptions options = FcmMessageOptions.builder()
                            .setPriorityEnum(PriorityEnum.High)
                            .setContentAvailable(true)
                            .setCollapseKey("collapseKey")
                            .setTimeToLive(Duration.ofMinutes(30))
                            .build();

                    notifications.forEach(notification -> {

                        Map<String, Object> data = new HashMap<>();

                        data.put("title", notification.getTitle());
                        data.put("endTime", notification.getEndTime());
                        data.put("lat", notification.getGeoPoint().getLatitude());
                        data.put("lng", notification.getGeoPoint().getLongitude());

                        DataUnicastMessage message = new DataUnicastMessage(options, notification.getToken(), data);
                        List<FcmMessageResultItem> results = client.send(message).getResults();

                        if(results.stream().allMatch(fmc -> fmc.getErrorCode() == null))
                            notification.setSent(true);
                    });
                }
            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            } finally {
                // Update the notification collection with isSent flag changed.
                notifications.forEach(notification ->
                        database.getCollection(NOTIFICATIONS)
                                .document(notification.getId())
                                .update("isSent", notification.isSent()));
            }
        });
    }
}