package com.navimee.models.entities.places;

import java.util.List;

public class FsTimeFrame {
    private String days;
    private List<String> open;

    public FsTimeFrame(){}

    public FsTimeFrame(String days, List<String> open){
        this.days = days;
        this.open = open;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public List<String> getOpen() {
        return open;
    }

    public void setOpen(List<String> open) {
        this.open = open;
    }
}
