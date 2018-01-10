package com.navimee.configuration.specific;

import com.navimee.configuration.Configuration;

public class FacebookConfiguration extends Configuration {

    private static final String facebookApi = "https://graph.facebook.com/";

    public FacebookConfiguration() {
        super(getConfigVar("FACEBOOK_TOKEN"),
                getConfigVar("FACEBOOK_CLIENT_ID"),
                getConfigVar("FACEBOOK_CLIENT_SECRET"),
                facebookApi);
    }
}
