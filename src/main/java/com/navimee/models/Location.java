package com.navimee.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {

    public String id;
    public String name;
    public String zip;
    public String country;
    public double latitude;
    public double longitude;

    @JsonProperty("location")
    private void getLocation(Map<String, String> json) {
        zip = json.get("zip");
        country = json.get("country");
        latitude = Double.parseDouble(json.get("latitude"));
        longitude = Double.parseDouble(json.get("longitude"));
    }
}
