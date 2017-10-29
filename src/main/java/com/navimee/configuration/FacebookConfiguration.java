package com.navimee.configuration;

import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class FacebookConfiguration extends Configuration {

    private final JSONObject config;

    public String apiUrl;
    public String clientId;
    public String clientSecret;
    public String accessToken;
    public String eventsPath;

    public FacebookConfiguration(Resource facebookConfig) throws IOException {

        config = transformConfig(facebookConfig);

        apiUrl = config.getString("apiUrl");
        clientId = config.getString("clientId");
        clientSecret = config.getString("clientSecret");
        accessToken = config.getString("accessToken");
        eventsPath = config.getString("eventsPath");
    }
}
