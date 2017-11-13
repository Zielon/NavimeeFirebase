package com.navimee.configuration.specific;

import com.navimee.configuration.Configuration;
import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class FoursquareConfiguration extends Configuration {

    private final JSONObject config;

    public final String apiUrl;
    public final String clientId;
    public final String clientSecret;

    public FoursquareConfiguration(Resource foursquareConfig) throws IOException {

        config = transformConfig(foursquareConfig);
        apiUrl = config.getString("apiUrl");
        clientId = config.getString("clientId");
        clientSecret = config.getString("clientSecret");
    }
}
