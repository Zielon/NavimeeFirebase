package com.navimee.contracts.models.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.navimee.contracts.models.places.pojo.AddressComponents;
import com.navimee.contracts.models.places.pojo.Geometry;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlace {
    @JsonProperty("formatted_address")
    public String formattedAddress;

    @JsonProperty("address_components")
    public List<AddressComponents> addressComponents;

    public Geometry geometry;
}
