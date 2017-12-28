package com.navimee.models.entities.coordinates;

import com.navimee.models.entities.contracts.Entity;

public class Coordinate implements Entity {
    private Double latitude;
    private Double longitude;
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
