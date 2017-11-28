package com.navimee.models.entities.places.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.GeoPoint;
import com.navimee.models.entities.places.Place;

import java.util.Map;

public class FbPlace extends Place {
    private String category;
    private GeoPoint geoPoint;

    @JsonProperty("geoPoint")
    private void getGeoPoint(Map<String, Double> json) {
        geoPoint = new GeoPoint(json.get("latitude"), json.get("longitude"));
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
