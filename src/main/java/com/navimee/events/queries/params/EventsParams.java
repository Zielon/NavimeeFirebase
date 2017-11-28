package com.navimee.events.queries.params;

import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.queries.QueryParams;

import java.util.List;

public class EventsParams implements QueryParams {
    public List<FbPlace> places;

    public EventsParams(List<FbPlace> places) {
        this.places = places;
    }
}
