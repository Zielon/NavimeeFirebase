package com.navimee.enums;

import static com.navimee.firestore.Paths.*;

public enum CollectionEnum {
    EVENTS(EVENTS_COLLECTION),
    FACEBOOK_PLACES(FACEBOOK_PLACES_COLLECTION),
    FOURSQUARE_PLACES(FOURSQUARE_PLACES_COLLECTION),
    COORDINATES(COORDINATES_COLLECTION),
    AVAILABLE_CITIES(AVAILABLE_CITIES_COLLECTION),
    SEGREGATED_EVENTS(SEGREGATED_EVENTS_COLLECTION),
    FOURSQUARE_PLACES_DETAILS(FOURSQUARE_PLACES_DETAILS_COLLECTION);

    private final String path;

    CollectionEnum(final String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
