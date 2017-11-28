package com.navimee.events.queries.params;

import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.queries.QueryParams;

public class EventsParams implements QueryParams {
    public FbPlace place;

    public EventsParams(FbPlace place) {
        this.place = place;
    }
}
