package com.navimee.models.externalDto.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.navimee.models.entities.places.Place;
import com.navimee.models.externalDto.BaseDto;
import com.navimee.models.externalDto.places.PlaceDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FbEventDto implements BaseDto {
    private String name;
    private String id;
    private String category;
    private String type;
    private PlaceDto place;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("end_time")
    private String endTime;

    @JsonProperty("attending_count")
    private long attendingCount;

    @JsonProperty("maybe_count")
    private long maybeCount;

    // REMEMBER THE SEARCH PLACE
    @JsonIgnore
    private Place searchPlace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PlaceDto getPlace() {
        return place;
    }

    public void setPlace(PlaceDto place) {
        this.place = place;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getAttendingCount() {
        return attendingCount;
    }

    public void setAttendingCount(long attendingCount) {
        this.attendingCount = attendingCount;
    }

    public long getMaybeCount() {
        return maybeCount;
    }

    public void setMaybeCount(long maybeCount) {
        this.maybeCount = maybeCount;
    }

    public Place getSearchPlace() {
        return searchPlace;
    }

    public void setSearchPlace(Place searchPlace) {
        this.searchPlace = searchPlace;
    }
}
