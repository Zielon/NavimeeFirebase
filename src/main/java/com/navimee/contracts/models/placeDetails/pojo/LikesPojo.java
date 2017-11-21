package com.navimee.contracts.models.placeDetails.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LikesPojo {
    public int count;
    public String summary;
}
