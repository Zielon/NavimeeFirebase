package com.navimee.contracts.models.places.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {

    public double lat;
    public double lon;

    @JsonProperty("location")
    private void getLocation(Map<String, String> json) {
        lat = Double.parseDouble(json.get("lat"));
        lon = Double.parseDouble(json.get("lng"));
    }
}
