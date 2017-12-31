package com.navimee.queries.places.params;

public class PlaceDetailsParams extends PlacesParams {

    public String type;
    public String placeId;

    public PlaceDetailsParams(double lat, double lon, String type) {
        super(lat, lon);
        this.type = type;
    }

    public PlaceDetailsParams(String type, String placeId) {
        this.type = type;
        this.placeId = placeId;
    }
}
