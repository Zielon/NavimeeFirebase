package com.navimee.models.externalDto.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.externalDto.BaseDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDto implements BaseDto {
    public String name;
    public String id;
    public double lat;
    public double lon;
    public String city;
    public String address;
}
