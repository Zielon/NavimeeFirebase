package com.navimee.places.queries.params;

import com.navimee.queries.QueryParams;

public class PlacesParams extends QueryParams {

    public double lat;
    public double lon;
    public String type;
    public String placeId;

    public PlacesParams(double lat, double lon) {
        this.lon = lon;
        this.lat = lat;
    }

    public PlacesParams(double lat, double lon, String type) {
        this.lon = lon;
        this.lat = lat;
        this.type = type;
    }

    public PlacesParams(String type, String placeId) {
        this.type = type;
        this.placeId = placeId;
    }
}
