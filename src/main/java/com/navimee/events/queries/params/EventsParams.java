package com.navimee.events.queries.params;

import com.navimee.contracts.models.places.Place;
import com.navimee.queries.QueryParams;

public class EventsParams extends QueryParams {
    public Place place;

    public EventsParams(Place place) {
        this.place = place;
    }
}
