package com.navimee.configuration.specific;

import com.navimee.configuration.Configuration;
import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class GoogleConfiguration extends Configuration {

    private final JSONObject config;

    public final String apiUrl;
    public final String clientId;
    public final String fmcApiKey;
    public final String fmcApiUrl;

    public GoogleConfiguration(Resource googleApi, Resource googleFmc) throws IOException {

        config = transformConfig(googleApi);
        apiUrl = config.getString("apiUrl");
        clientId = config.getString("clientId");

        JSONObject fmc = transformConfig(googleFmc);
        fmcApiKey = fmc.getString("fcm.api.key");
        fmcApiUrl = fmc.getString("fcm.api.url");
    }
}
