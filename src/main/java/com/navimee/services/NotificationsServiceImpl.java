package com.navimee.services;

import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.repositories.UsersRepository;
import com.navimee.contracts.services.NotificationsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.bo.FbEvent;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.User;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.client.settings.PropertiesBasedSettings;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.requests.notification.NotificationPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class NotificationsServiceImpl implements NotificationsService {

    @Autowired
    ExecutorService executorService;

    @Autowired
    GoogleConfiguration googleConfiguration;

    @Autowired
    UsersRepository usersRepository;

    @Override
    public Future send() {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogEnum.TASK, "Send notifications"));

            List<User> users = usersRepository.getUsersForNotification();

            Properties properties = new Properties();
            properties.setProperty("fcm.api.url", googleConfiguration.fmcApiUrl);
            properties.setProperty("fcm.api.key", googleConfiguration.fmcApiKey);

            try {
                try (FcmClient client = new FcmClient(PropertiesBasedSettings.createFromProperties(properties))) {

                    FcmMessageOptions options = FcmMessageOptions.builder().setTimeToLive(Duration.ofHours(1)).build();

                    users.forEach(user -> {
                        NotificationPayload payload =
                                NotificationPayload.builder()
                                        .setBody("Events for " + user.getEmail())
                                        .setTitle("Warning")
                                        .setTag("Events less than 30 minutes from now!")
                                        .build();

                        Map<String, FbEvent> data = new HashMap<>();

                        DataUnicastMessage message = new DataUnicastMessage(options, user.getToken(), data, payload);
                        client.send(message).getResults();
                    });
                }
            } catch (Exception e) {
                Logger.LOG(new Log(LogEnum.EXCEPTION, e));
            }
        });
    }
}
