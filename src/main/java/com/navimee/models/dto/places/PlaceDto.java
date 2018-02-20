package com.navimee.models.dto.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.dto.Dto;

import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDto implements Dto {
    protected String name;
    protected String id;
    protected String city;
    protected String address;
    protected double lat;
    protected double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getFullAddress(){
        String address = new StringJoiner(",").add(this.city).add(this.address).toString();
        return address.split("null").length > 1 ? "" : address;
    }
}
