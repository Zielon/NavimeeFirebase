package com.navimee.contracts.models.dataTransferObjects.placeDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.contracts.models.dataTransferObjects.placeDetails.subelements.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoursquarePlaceDetailsDto {

    public String id;
    public String name;
    public double rating;
    public LocationDto location;
    public StatsDto stats;
    public PopularDto popular;
    public LikesDto likes;
    public List<CategoryDto> categories;
}
