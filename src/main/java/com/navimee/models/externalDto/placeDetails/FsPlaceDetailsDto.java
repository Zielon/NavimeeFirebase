package com.navimee.models.externalDto.placeDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.externalDto.BaseDto;
import com.navimee.models.externalDto.placeDetails.subelements.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FsPlaceDetailsDto implements BaseDto {
    public String id;
    public String name;
    public double rating;
    public LocationDto location;
    public StatsDto stats;
    public PopularDto popular;
    public LikesDto likes;
    public List<CategoryDto> categories;
}
