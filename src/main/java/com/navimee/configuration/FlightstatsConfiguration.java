package com.navimee.configuration;

import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class FlightstatsConfiguration extends Configuration {

    private final JSONObject config;

    public FlightstatsConfiguration(Resource flightstastConfig) throws IOException {
        config = transformConfig(flightstastConfig);
    }

    @Override
    public String getAccessToken() {
        String appId = config.getString("appId");
        String appSecrets = config.getString("appSecrets");
        return String.format("?appId=%s?appKey=%s", appId, appSecrets);
    }

    @Override
    public JSONObject getJSONObject() {
        return config;
    }
}
