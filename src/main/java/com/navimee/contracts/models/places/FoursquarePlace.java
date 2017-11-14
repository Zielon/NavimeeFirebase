package com.navimee.contracts.models.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoursquarePlace extends Place {

    public String facebook;
    public int hereNow;

    @JsonProperty("contact")
    private void getName(Map<String, String> json) {
        facebook = json.getOrDefault("facebook", null);
    }

    @JsonProperty("location")
    private void getLocation(Map<String, Object> json) {
        lat = Double.parseDouble(json.get("lat").toString());
        lon = Double.parseDouble(json.get("lng").toString());
        city = json.containsKey("city") ? json.get("city").toString() : null;
        address = json.containsKey("address") ? json.get("address").toString() : null;
    }

    @JsonProperty("hereNow")
    private void gethereNow(Map<String, Object> json) {
        hereNow = Integer.parseInt(json.get("count").toString());
    }

    @Override
    public String getId() {
        return facebook;
    }
}
