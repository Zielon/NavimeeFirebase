package com.navimee.services;

import com.navimee.contracts.services.NotificationsService;
import com.navimee.models.entities.User;
import com.navimee.models.entities.events.FbEvent;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.client.settings.PropertiesBasedSettings;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.requests.notification.NotificationPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    @Override
    public Future send(List<User> users) {

        Properties properties = new Properties();

        properties.setProperty("fcm.api.url",
                "https://fcm.googleapis.com/fcm/send");

        properties.setProperty("fcm.api.key",
                "AAAAAW_4DB4:APA91bG2UbOicKqji27KgKjtSshte_kXVZnN0_BS17PnoM4jdXcsJWk_8AFDR4BLHEGq7tlKzfOBjK-SlxphOip4LVjdayUUtftPifTH1ahRRlUtmUIOhZSxMAKM4zm42_yYscC4zGgQ");

        return executorService.submit(() -> {
            try {
                try (FcmClient client = new FcmClient(PropertiesBasedSettings.createFromProperties(properties))) {

                    FcmMessageOptions options = FcmMessageOptions.builder().setTimeToLive(Duration.ofHours(1)).build();

                    users.forEach(user -> {
                        NotificationPayload payload = NotificationPayload.builder().setBody("Events for " + user.getEmail()).setTitle("Title").setTag("Tag").build();
                        Map<String, FbEvent> data = user.getEvents().stream().collect(Collectors.toMap(FbEvent::getId, event -> event));

                        DataUnicastMessage message = new DataUnicastMessage(options, user.getToken(), data, payload);
                        client.send(message);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
