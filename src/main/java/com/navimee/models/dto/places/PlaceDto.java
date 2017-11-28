package com.navimee.models.dto.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.dto.Dto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDto implements Dto {
    protected String name;
    protected String id;
    protected String city;
    protected String address;

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
