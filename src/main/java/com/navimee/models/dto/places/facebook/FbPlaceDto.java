package com.navimee.models.dto.places.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.GeoPoint;
import com.navimee.models.dto.places.PlaceDto;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FbPlaceDto extends PlaceDto {
    private String category;
    private GeoPoint geoPoint;

    @JsonProperty("location")
    private void getLocation(Map<String, String> json) {
        Double lat = Double.parseDouble(json.get("latitude"));
        Double lon = Double.parseDouble(json.get("longitude"));
        geoPoint = new GeoPoint(lat, lon);
        city = json.get("city");
        address = json.get("street");
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}