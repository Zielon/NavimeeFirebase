package com.navimee.configuration.specific;

import com.navimee.configuration.Configuration;

public class GoogleFcmConfiguration extends Configuration {

    private static final String googleFcmApi = "https://fcm.googleapis.com/fcm/send";

    public GoogleFcmConfiguration() {
        super("", "", getConfigVar("GOOGLE_FCM_API_KEY"), googleFcmApi);
    }
}
