package com.navimee.contracts.models.dataTransferObjects.placeDetails.subelements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto {
    public String id;
    public String name;
    public String pluralName;
    public String shortName;
}
