package com.navimee.models.externalDto.events;

import com.navimee.models.entities.places.Place;
import com.navimee.models.externalDto.BaseDto;
import com.navimee.models.externalDto.places.PlaceDto;

public class FbEventDto implements BaseDto {
    public String name;
    public String id;
    public String category;
    public String start_time;
    public String end_time;
    public long attending_count;
    public long maybe_count;
    public String type;
    public PlaceDto place;

    // REMEMBER THE SEARCH PLACE
    public Place searchPlace;
}
