package com.navimee.models.entities;

public class Coordinate {

    public Double latitude;
    public Double longitude;
    public String street;

    public Coordinate() {
    }

    public Coordinate(Double latitude, Double longitude, String street) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
    }
}
