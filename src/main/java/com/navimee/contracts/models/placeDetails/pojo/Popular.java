package com.navimee.contracts.models.placeDetails.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Popular {
    public String status;
    public boolean isOpen;
    public boolean isLocalHoliday;
    public List<TimeFrames> timeframes;
}
