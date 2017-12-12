package com.navimee.models.dto.timeframes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.dto.Dto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PopularDto implements Dto {
    @JsonIgnore
    private String placeId;
    private List<TimeFrameDto> timeframes;

    public List<TimeFrameDto> getTimeframes() {
        return timeframes;
    }

    public void setTimeframes(List<TimeFrameDto> timeframes) {
        this.timeframes = timeframes;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
