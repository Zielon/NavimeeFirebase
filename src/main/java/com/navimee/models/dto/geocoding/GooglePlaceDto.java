package com.navimee.models.dto.geocoding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.navimee.models.dto.Dto;
import com.navimee.models.dto.places.subelement.AddressComponentsDto;
import com.navimee.models.dto.places.subelement.GeometryDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlaceDto implements Dto {
    @JsonProperty("formatted_address")
    public String formattedAddress;

    @JsonProperty("address_components")
    public List<AddressComponentsDto> addressComponents;

    public GeometryDto geometry;
}
