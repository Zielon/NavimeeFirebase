package com.navimee.configuration.specific;

import com.navimee.configuration.Configuration;

public class GoogleGeoConfiguration extends Configuration {

    private static final String googleGeoApi = "https://maps.googleapis.com/";

    public GoogleGeoConfiguration() {
        super("", "", getConfigVar("GOOGLE_GEO_API_KEY"), googleGeoApi);
    }
}
