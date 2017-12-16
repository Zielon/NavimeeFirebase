package com.navimee.configuration.specific;

import com.navimee.configuration.Configuration;
import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class PredictHqConfiguration extends Configuration {

    private final JSONObject config;

    public final String accessToken;
    public final String apiUrl;

    public PredictHqConfiguration(Resource predictHqConfig) throws IOException {

        this.config = transformConfig(predictHqConfig);

        accessToken = config.getString("accessToken");
        apiUrl = config.getString("apiUrl");
    }
}
