package com.navimee.models.dto.places.foursquare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.navimee.models.dto.places.PlaceDto;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FsPlaceDto extends PlaceDto {
    private String facebook;

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

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
}
