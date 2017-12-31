package com.navimee.queries.events.params;

import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.queries.QueryParams;

import java.util.List;

public class FacebookEventsParams implements QueryParams {
    public List<FbPlace> places;

    public FacebookEventsParams(List<FbPlace> places) {
        this.places = places;
    }
}
