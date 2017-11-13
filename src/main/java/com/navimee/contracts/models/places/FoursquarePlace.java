package com.navimee.contracts.models.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoursquarePlace extends Place {

    public String facebook;

    @JsonProperty("contact")
    private void getName(Map<String, String> json) {
        facebook = json.get("facebook");
    }

    @JsonProperty("location")
    private void getLocation(Map<String, Object> json) {
        lat = Double.parseDouble(json.get("lat").toString());
        lon = Double.parseDouble(json.get("lng").toString());
        city = json.get("city").toString();
        address = json.get("address").toString();
    }
}
