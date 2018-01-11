package com.navimee.configuration.specific;

import com.navimee.configuration.Configuration;

public class FoursquareConfiguration extends Configuration {

    private static final String foursquareApi = "https://api.foursquare.com/";

    public FoursquareConfiguration() {
        super("", getConfigVar("FOURSQUARE_CLIENT_ID"), getConfigVar("FOURSQUARE_CLIENT_SECRET"), foursquareApi);
    }
}
