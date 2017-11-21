package com.navimee.configuration.specific;

import com.navimee.configuration.Configuration;
import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class GoogleConfiguration extends Configuration {

    private final JSONObject config;

    public final String apiUrl;
    public final String clientId;

    public GoogleConfiguration(Resource facebookConfig) throws IOException {

        config = transformConfig(facebookConfig);
        apiUrl = config.getString("apiUrl");
        clientId = config.getString("clientId");
    }
}
