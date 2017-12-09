package com.navimee.models.entities.coordinates;

import com.navimee.models.entities.Entity;

import java.util.UUID;

public class City implements Entity {
    private String id;
    private String name;
    private String internalId;

    public String getId() {
        return id;
    }

    @Override
    public String getInternalId() {
        return internalId;
    }

    @Override
    public void setInternalId(UUID uuid) {
        this.internalId = uuid.toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
