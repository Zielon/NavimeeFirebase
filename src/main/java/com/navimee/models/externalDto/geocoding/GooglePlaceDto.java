package com.navimee.models.externalDto.geocoding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.navimee.models.externalDto.BaseDto;
import com.navimee.models.externalDto.places.subelement.AddressComponentsDto;
import com.navimee.models.externalDto.places.subelement.GeometryDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlaceDto implements BaseDto {
    @JsonProperty("formatted_address")
    public String formattedAddress;

    @JsonProperty("address_components")
    public List<AddressComponentsDto> addressComponents;

    public GeometryDto geometry;
}
