package com.navimee.services;

import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.services.NotificationsService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.User;
import com.navimee.models.entities.events.FbEvent;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.client.settings.PropertiesBasedSettings;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.requests.notification.NotificationPayload;
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
import java.util.stream.Collectors;

@Service
public class NotificationsServiceImpl implements NotificationsService {

    @Autowired
    ExecutorService executorService;

    @Autowired
    GoogleConfiguration googleConfiguration;

    @Override
    public Future send(List<User> users) {

        Properties properties = new Properties();
        properties.setProperty("fcm.api.url", googleConfiguration.fmcApiUrl);
        properties.setProperty("fcm.api.key", googleConfiguration.fmcApiKey);

        return executorService.submit(() -> {
            try {
                try (FcmClient client = new FcmClient(PropertiesBasedSettings.createFromProperties(properties))) {

                    FcmMessageOptions options = FcmMessageOptions.builder().setTimeToLive(Duration.ofHours(1)).build();

                    users.forEach(user -> {
                        NotificationPayload payload = NotificationPayload.builder().setBody("Events for " + user.getEmail()).setTitle("Title").setTag("Tag").build();
                        Map<String, FbEvent> data = new HashMap<>();
                        DataUnicastMessage message = new DataUnicastMessage(options, user.getToken(), data, payload);
                        List<FcmMessageResultItem> results = client.send(message).getResults();
                        results.size();
                    });
                }
            } catch (Exception e) {
                Logger.LOG(new Log(LogEnum.EXCEPTION, e));
            }
        });
    }
}
