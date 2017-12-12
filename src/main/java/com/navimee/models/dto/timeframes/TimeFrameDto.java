package com.navimee.models.dto.timeframes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.dto.Dto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeFrameDto implements Dto {
    private List<Integer> days;
    private List<OpenDto> open;

    public List<OpenDto> getOpen() {
        return open;
    }

    public void setOpen(List<OpenDto> open) {
        this.open = open;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }
}