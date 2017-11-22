package com.navimee.models.externalDto.placeDetails.subelements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto {
    public String id;
    public String name;
    public String pluralName;
    public String shortName;
}
