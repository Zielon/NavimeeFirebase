package com.navimee.models.bussinesObjects.places;

public class PlaceBo {
    protected String name;
    protected String id;
    protected double lat;
    protected double lon;
    protected String city;
    protected String address;

    public PlaceBo(String name,String id, double lat, double lon, String city, String address){
        this.name = name;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.city = city;
        this.address = address;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
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
}
