package com.navimee.events.queries.params;

import com.navimee.queries.QueryParams;

public class PredictHqEventsParams implements QueryParams {
    public double lat;
    public double lon;

    public PredictHqEventsParams(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
    }
}
