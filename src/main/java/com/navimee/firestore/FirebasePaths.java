package com.navimee.firestore;

import com.google.cloud.firestore.CollectionReference;

public class FirebasePaths {

    // Collections places
    public static final String COORDINATES = "COORDINATES";
    public static final String AVAILABLE_CITIES = "AVAILABLE_CITIES";
    public static final String FACEBOOK_PLACES = "FACEBOOK_PLACES";
    public static final String FOURSQUARE_PLACES = "FOURSQUARE_PLACES";
    public static final String FOURSQUARE_PLACES_DETAILS = "FOURSQUARE_PLACES_DETAILS";
    public static final String ROOM_DETAILS = "ROOM_DETAILS";
    public static final String GROUP = "GROUP";

    // Documents
    public static final String NOTIFICATIONS = "NOTIFICATIONS";
    public static final String EVENTS_NOTIFICATION = "EVENTS_NOTIFICATION";

    // Users
    public static final String USERS = "USERS";

    // Path
    public static final String CITIES = "CITIES";

    // Feedback
    public static final String FEEDBACK_COLLECTION = "FEEDBACK";

    // Logger
    public static final String LOGS = "EXECUTION_LOGS";

    // Hotspot
    public static final String HOTSPOT = "HOTSPOT";
    public static final String HOTSPOT_CURRENT = "HOTSPOT_CURRENT";
    public static final String USER_LOCATION = "USER_LOCATION";

    public static String get(CollectionReference collectionReference) {
        String path = collectionReference.getPath();
        return path.toUpperCase().split("DOCUMENTS")[1];
    }

    public static String get(String collectionReference) {
        String path = collectionReference.toUpperCase();
        return path.contains("DOCUMENTS") ? path.split("DOCUMENTS")[1] : path;
    }
}
