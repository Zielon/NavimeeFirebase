package com.navimee.contracts.models.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookPlace extends Place {
    public String category;

    @JsonProperty("location")
    private void getLocation(Map<String, String> json) {
        lat = Double.parseDouble(json.get("latitude"));
        lon = Double.parseDouble(json.get("longitude"));
        city = json.get("city");
        address = json.get("street");
    }
}
