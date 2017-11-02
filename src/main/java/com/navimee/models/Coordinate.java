package com.navimee.models;

public class Coordinate {

    public String city;
    public String country;
    public Double latitude;
    public Double longitude;
    public String street;

    public Coordinate() {
    }

    public Coordinate(String city, String country, Double latitude, Double longitude, String street) {
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
    }

    @Override
    public String toString() {
        return String.format("%d -> %d", city, country);
    }
}
