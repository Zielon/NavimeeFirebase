package com.navimee.models.entities.places.foursquare.popularHours;

import java.util.List;

public class FsPopular {
    private List<FsTimeFrame> timeframes;

    public List<FsTimeFrame> getTimeframes() {
        return timeframes;
    }

    public void setTimeframes(List<FsTimeFrame> timeframes) {
        this.timeframes = timeframes;
    }
}
