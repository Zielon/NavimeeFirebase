package com.navimee.contracts.models.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookPlace extends Place {
    public String category;
}
