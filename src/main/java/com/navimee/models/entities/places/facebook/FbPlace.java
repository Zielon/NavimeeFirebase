package com.navimee.models.entities.places.facebook;

import com.navimee.models.entities.places.Place;

public class FbPlace extends Place {
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
