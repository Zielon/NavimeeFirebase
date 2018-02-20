package com.navimee.models.entities.places.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.navimee.models.entities.places.Place;

import java.util.Map;

public class FbPlace extends Place {
    private String category;

    public FbPlace() {
    }

    public FbPlace(String id) {
        this.setId(id);
    }

    @JsonProperty("geoPoint")
    private void getGeoPoint(Map<String, Double> json) {
       lat = json.get("latitude");
       lon = json.get("longitude");
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
