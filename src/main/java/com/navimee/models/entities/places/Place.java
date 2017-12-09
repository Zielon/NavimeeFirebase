package com.navimee.models.entities.places;

import com.navimee.models.entities.Entity;

import java.util.UUID;

public class Place implements Entity {
    private String name;
    private String id;
    private String city;
    private String address;
    private String internalId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
