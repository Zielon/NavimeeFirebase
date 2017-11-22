package com.navimee.models.externalDto.places.subelement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeometryDto {

    public double lat;
    public double lon;

    @JsonProperty("location")
    private void getLocation(Map<String, String> json) {
        lat = Double.parseDouble(json.get("lat"));
        lon = Double.parseDouble(json.get("lng"));
    }
}
