package com.navimee.models.dto.places.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.GeoPoint;
import com.navimee.models.dto.places.PlaceDto;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FbPlaceDto extends PlaceDto {
    private String category;

    @JsonProperty("location")
    private void getLocation(Map<String, String> json) {
        this.lat = Double.parseDouble(json.get("latitude"));
        this.lon = Double.parseDouble(json.get("longitude"));
        this.city = json.get("city");
        this.address = json.get("street");
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}