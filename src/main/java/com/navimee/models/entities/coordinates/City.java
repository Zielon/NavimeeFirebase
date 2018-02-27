package com.navimee.models.entities.coordinates;

import com.google.cloud.firestore.annotation.Exclude;
import com.navimee.models.entities.contracts.Entity;

public class City implements Entity {
    private String name;

    @Exclude
    public String getId() {
        return Integer.toString(name.hashCode());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
