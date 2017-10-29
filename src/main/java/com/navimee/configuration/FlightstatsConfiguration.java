package com.navimee.configuration;

import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class FlightstatsConfiguration extends Configuration {

    private final JSONObject config;

    public String apiUrl;
    public String clientId;
    public String clientSecret;
    public String accessToken;

    public FlightstatsConfiguration(Resource flightstastConfig) throws IOException {
        config = transformConfig(flightstastConfig);

        clientId = config.getString("clientId");
        clientSecret = config.getString("clientSecret");
    }
}
