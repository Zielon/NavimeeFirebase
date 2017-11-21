package com.navimee.contracts.models.dataTransferObjects.placeDetails.subelements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PopularDto {
    public String status;
    public boolean isOpen;
    public boolean isLocalHoliday;
    public List<TimeFramesDto> timeframes;
}
