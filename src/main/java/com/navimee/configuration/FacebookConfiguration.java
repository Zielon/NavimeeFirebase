package com.navimee.configuration;

import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class FacebookConfiguration extends Configuration {

    private final JSONObject config;

    public final String apiUrl;
    public final String clientId;
    public final String clientSecret;
    public final String accessToken;

    public FacebookConfiguration(Resource facebookConfig) throws IOException {

        config = transformConfig(facebookConfig);
        apiUrl = config.getString("apiUrl");
        clientId = config.getString("clientId");
        clientSecret = config.getString("clientSecret");
        accessToken = config.getString("accessToken");
    }
}
