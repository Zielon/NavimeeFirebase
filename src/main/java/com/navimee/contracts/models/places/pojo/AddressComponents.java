package com.navimee.contracts.models.places.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AddressComponents {

    @JsonProperty("long_name")
    public String longName;

    @JsonProperty("short_name")
    public String shortName;

    @JsonProperty("types")
    public List<String> types;
}
