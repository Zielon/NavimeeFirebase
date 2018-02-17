package com.navimee.firestore;

import com.google.cloud.firestore.CollectionReference;

public class FirebasePaths {

    // Collections places
    public static final String COORDINATES = "COORDINATES";
    public static final String AVAILABLE_CITIES = "AVAILABLE_CITIES";
    public static final String FACEBOOK_PLACES = "FACEBOOK_PLACES";
    public static final String FOURSQUARE_PLACES = "FOURSQUARE_PLACES";
    public static final String FOURSQUARE_PLACES_DETAILS = "FOURSQUARE_PLACES_DETAILS";

    // Documents
    public static final String NOTIFICATIONS = "NOTIFICATIONS";

    // Users
    public static final String USERS = "USERS";

    // Path
    public static final String BY_CITY = "BY_CITY";

    // Feedback
    public static final String FEEDBACK_COLLECTION = "FEEDBACK";

    // Logger
    public static final String LOGS = "EXECUTION_LOGS";

    // Hotspot
    public static final String HOTSPOT = "HOTSPOT";
    public static final String HOTSPOT_CURRENT = "HOTSPOT_CURRENT";

    public static String get(CollectionReference collectionReference) {
        String path = collectionReference.getPath();
        return path.toUpperCase().split("DOCUMENTS")[1];
    }

    public static String get(String collectionReference) {
        String path = collectionReference.toUpperCase();
        return path.contains("DOCUMENTS") ? path.split("DOCUMENTS")[1] : path;
    }
}
