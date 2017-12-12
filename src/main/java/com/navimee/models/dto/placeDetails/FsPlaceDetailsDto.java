package com.navimee.models.dto.placeDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.dto.Dto;
import com.navimee.models.dto.placeDetails.subelements.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FsPlaceDetailsDto implements Dto {
    public String id;
    public String name;
    public double rating;
    public LocationDto location;
    public StatsDto stats;
    public LikesDto likes;
    public List<CategoryDto> categories;
}
