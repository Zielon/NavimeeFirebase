package com.navimee.models.entities.coordinates;

import com.navimee.models.entities.contracts.Entity;

public class City implements Entity {
    private String name;
    private String id;

    public String getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {

        this.id = id;
    }
}
