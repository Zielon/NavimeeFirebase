package com.navimee.contracts.models.placeDetails.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {
    public String id;
    public String name;
    public String pluralName;
    public String shortName;
}
