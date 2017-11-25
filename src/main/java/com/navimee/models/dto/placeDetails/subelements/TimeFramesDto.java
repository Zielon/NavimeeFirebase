package com.navimee.models.dto.placeDetails.subelements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeFramesDto {
    public String days;
    public List<OpenDto> open;
}
