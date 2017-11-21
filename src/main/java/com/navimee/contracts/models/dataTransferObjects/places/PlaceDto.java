package com.navimee.contracts.models.dataTransferObjects.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDto {
    public String name;
    public String id;
    public double lat;
    public double lon;
    public String city;
    public String address;
}
