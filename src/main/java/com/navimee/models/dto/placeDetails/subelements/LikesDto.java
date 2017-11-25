package com.navimee.models.dto.placeDetails.subelements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LikesDto {
    public int count;
    public String summary;
}
