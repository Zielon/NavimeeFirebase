package com.navimee.models.entities.coordinates;

import com.google.cloud.firestore.annotation.Exclude;
import com.navimee.models.entities.contracts.Entity;

public class Coordinate implements Entity {
    private Double latitude;
    private Double longitude;

    public Coordinate() {
    }

    public Coordinate(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Exclude
    public String getId() {
        return Integer.toString(latitude.hashCode() + longitude.hashCode());
    }
}
