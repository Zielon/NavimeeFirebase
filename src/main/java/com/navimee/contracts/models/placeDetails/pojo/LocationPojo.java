package com.navimee.contracts.models.placeDetails.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationPojo {
    public String address;
    public String crossStreet;
    public String postalCode;
    public String city;
    public String country;
    public double lat;
    public double lng;
}
