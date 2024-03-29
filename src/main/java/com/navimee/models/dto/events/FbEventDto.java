package com.navimee.models.dto.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.navimee.models.dto.Dto;
import com.navimee.models.dto.places.facebook.FbPlaceDto;
import com.navimee.models.entities.places.facebook.FbPlace;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FbEventDto implements Dto {
    private String name;
    private String id;
    private String category;
    private String type;
    private FbPlaceDto place;

    @JsonProperty("start_time")
    private Date startTime;

    @JsonProperty("end_time")
    private Date endTime;

    @JsonProperty("attending_count")
    private long attendingCount;

    @JsonProperty("maybe_count")
    private long maybeCount;

    // REMEMBER THE SEARCH PLACE
    @JsonIgnore
    private FbPlace searchPlace;

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

    public FbPlaceDto getPlace() {
        return place;
    }

    public void setPlace(FbPlaceDto place) {
        this.place = place;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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

    public FbPlace getSearchPlace() {
        return searchPlace;
    }

    public void setSearchPlace(FbPlace searchPlace) {
        this.searchPlace = searchPlace;
    }
}
