package com.navimee.contracts.models.pojos.placeDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeFrames {
    public String days;
    public List<Open> open;
}
