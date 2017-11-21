package com.navimee.contracts.models.dataTransferObjects.events;

import com.navimee.contracts.models.dataTransferObjects.BaseDto;
import com.navimee.contracts.models.dataTransferObjects.places.PlaceDto;

public class EventDto extends BaseDto {
    public String name;
    public String id;
    public String category;
    public String start_time;
    public String end_time;
    public long attending_count;
    public long maybe_count;
    public String type;
    public PlaceDto place;
}
