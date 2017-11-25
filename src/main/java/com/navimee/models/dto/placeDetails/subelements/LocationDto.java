package com.navimee.models.dto.placeDetails.subelements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDto {
    public String address;
    public String crossStreet;
    public String postalCode;
    public String city;
    public String country;
    public double lat;
    public double lng;
}
