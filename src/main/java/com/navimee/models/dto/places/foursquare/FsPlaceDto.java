package com.navimee.models.dto.places.foursquare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.navimee.models.dto.places.PlaceDto;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FsPlaceDto extends PlaceDto {
    private String facebook;
    private double lat;
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

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
