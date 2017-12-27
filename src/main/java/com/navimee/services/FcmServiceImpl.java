package com.navimee.services;

import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.services.FcmService;
import com.navimee.firestore.Database;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.contracts.FcmSendable;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.client.settings.PropertiesBasedSettings;
import de.bytefish.fcmjava.model.enums.PriorityEnum;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.responses.FcmMessageResultItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

import static com.navimee.enums.CollectionType.NOTIFICATIONS;

@Service
public class FcmServiceImpl implements FcmService {

    @Autowired
    ExecutorService executorService;

    @Autowired
    GoogleConfiguration googleConfiguration;

    @Autowired
    Database database;

    @Override
    public <T extends FcmSendable> Future send(List<T> sendables, Function<T, Map<String, Object>> function) {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogTypes.TASK, "Send notifications"));

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

                    sendables.forEach(data -> {
                        DataUnicastMessage message = new DataUnicastMessage(options, data.getToken(), function.apply(data));
                        List<FcmMessageResultItem> results = client.send(message).getResults();

                        if (results.stream().allMatch(fmc -> fmc.getErrorCode() == null))
                            data.setSent(true);
                    });
                }
            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            } finally {
                // Update the notification collection with isSent flag changed.
                sendables.forEach(data ->
                        database.getCollection(NOTIFICATIONS)
                                .document(data.getId())
                                .update("isSent", data.isSent()));
            }
        });
    }
}