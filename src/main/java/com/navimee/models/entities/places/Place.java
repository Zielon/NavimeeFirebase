package com.navimee.models.entities.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.entities.contracts.Entity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Place implements Entity {
    private String name;
    private String id;
    private String city;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
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
