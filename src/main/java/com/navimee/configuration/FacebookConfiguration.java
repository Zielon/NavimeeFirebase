package com.navimee.configuration;

import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class FacebookConfiguration extends Configuration {

    private final JSONObject config;

    public FacebookConfiguration(Resource facebookConfig) throws IOException {
        config = transformConfig(facebookConfig);
    }

    @Override
    public String getAccessToken() {
        return config.getString("accessToken");
    }

    @Override
    public JSONObject getConfiguration() {
        return config;
    }
}
