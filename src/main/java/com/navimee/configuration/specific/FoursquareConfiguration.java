package com.navimee.configuration.specific;

import com.navimee.configuration.Configuration;
import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class FoursquareConfiguration extends Configuration {

    private final JSONObject config;

    public FoursquareConfiguration(Resource foursquareConfig) throws IOException {

        config = transformConfig(foursquareConfig);
    }
}
