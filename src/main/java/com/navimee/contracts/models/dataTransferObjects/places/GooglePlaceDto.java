package com.navimee.contracts.models.dataTransferObjects.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.navimee.contracts.models.dataTransferObjects.places.subelement.AddressComponentsDto;
import com.navimee.contracts.models.dataTransferObjects.places.subelement.GeometryDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlaceDto {
    @JsonProperty("formatted_address")
    public String formattedAddress;

    @JsonProperty("address_components")
    public List<AddressComponentsDto> addressComponents;

    public GeometryDto geometry;
}
