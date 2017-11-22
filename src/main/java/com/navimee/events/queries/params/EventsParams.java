package com.navimee.events.queries.params;

import com.navimee.models.entities.places.Place;
import com.navimee.queries.QueryParams;

public class EventsParams implements QueryParams {
    public Place place;

    public EventsParams(Place place) {
        this.place = place;
    }
}
