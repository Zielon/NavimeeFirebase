package com.navimee.places.queries.params;

import com.navimee.queries.QueryParams;

public class PlacesParams extends QueryParams {

    public double lat;
    public double lon;

    public PlacesParams(double lat, double lon) {
        this.lon = lon;
        this.lat = lat;
    }
}
