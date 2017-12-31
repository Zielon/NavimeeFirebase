package com.navimee.queries.places.params;

import com.navimee.queries.QueryParams;

public class PlacesParams implements QueryParams {

    public double lat;
    public double lon;

    public PlacesParams() {
    }

    public PlacesParams(double lat, double lon) {
        this.lon = lon;
        this.lat = lat;
    }
}
