package com.navimee.contracts.models.events.pojo;

import com.navimee.contracts.models.events.Location;
import com.navimee.contracts.models.places.Place;

public class Event {
    public String name;
    public String id;
    public String category;
    public String start_time;
    public String end_time;
    public long attending_count;
    public long maybe_count;
    public String type;
    public Location place;
    public Place searchPlace;
}
