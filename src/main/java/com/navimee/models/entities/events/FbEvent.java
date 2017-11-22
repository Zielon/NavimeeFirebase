package com.navimee.models.entities.events;

import com.navimee.models.entities.places.Place;

public class FbEvent {
    public String name;
    public String id;
    public String category;
    public String startTime;
    public String endTime;
    public long attendingCount;
    public long maybeCount;
    public String type;
    public Place place;
}
