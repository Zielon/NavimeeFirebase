package com.navimee.events.queries.params;

import com.navimee.contracts.models.dataTransferObjects.places.PlaceDto;
import com.navimee.queries.QueryParams;

public class EventsParams implements QueryParams {
    public PlaceDto place;

    public EventsParams(PlaceDto place) {
        this.place = place;
    }
}
