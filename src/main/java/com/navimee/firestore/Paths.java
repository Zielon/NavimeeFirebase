package com.navimee.firestore;

import com.google.cloud.firestore.CollectionReference;

public class Paths {

    // Collections places
    public static final String COORDINATES_COLLECTION = "COORDINATES";
    public static final String AVAILABLE_CITIES_COLLECTION = "AVAILABLE_CITIES";
    public static final String FACEBOOK_PLACES_COLLECTION = "FACEBOOK_PLACES";
    public static final String FOURSQUARE_PLACES_COLLECTION = "FOURSQUARE_PLACES";
    public static final String FOURSQUARE_PLACES_DETAILS_COLLECTION = "FOURSQUARE_PLACES_DETAILS";

    // Collections events
    public static final String EVENTS_COLLECTION = "EVENTS";
    public static final String SEGREGATED_EVENTS_COLLECTION = "SEGREGATED_EVENTS";

    // Documents
    public static final String PLACES_DOCUMENT = "PLACES";

    // Users
    public static final String USERS_COLLECTION = "USERS";
    public static final String USERS_EVENTS_COLLECTION = "USER_EVENTS";

    // Path
    public static final String BY_CITY = "BY_CITY";

    // Logger
    public static final String LOGS = "EXECUTION_LOGS";

    // Hotspot
    public static final String HOTSPOT = "HOTSPOT";

    public static String get(CollectionReference collectionReference) {
        String path = collectionReference.getPath();
        return path.toUpperCase().split("DOCUMENTS")[1];
    }
}
