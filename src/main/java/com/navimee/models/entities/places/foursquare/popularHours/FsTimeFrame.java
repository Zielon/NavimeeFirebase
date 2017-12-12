package com.navimee.models.entities.places.foursquare.popularHours;

import java.util.List;

public class FsTimeFrame {
    private List<Integer> days;
    private List<FsTimeOpen> open;

    public FsTimeFrame() {
    }

    public FsTimeFrame(List<Integer> days, List<FsTimeOpen> open) {
        this.days = days;
        this.open = open;
    }

    public List<FsTimeOpen> getOpen() {
        return open;
    }

    public void setOpen(List<FsTimeOpen> open) {
        this.open = open;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }
}