package com.navimee.contracts.models.placeDetails.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Likes {
    public int count;
    public String summary;
}
