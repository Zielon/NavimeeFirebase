package com.navimee.configuration.specific;

import com.navimee.configuration.Configuration;

public class PredictHqConfiguration extends Configuration {

    private static final String predicthqApi = "https://api.predicthq.com/";

    public PredictHqConfiguration() {
        super(getConfigVar("PREDICT_HQ_TOKEN"), getConfigVar("PREDICT_HQ_CLIENT_ID"), getConfigVar("PREDICT_HQ_CLIENT_SECRET"), predicthqApi);
    }
}
