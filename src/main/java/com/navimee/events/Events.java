package com.navimee.events;

import com.navimee.contracts.models.events.Event;
import com.navimee.contracts.models.places.GooglePlace;
import com.navimee.enums.EventsSegregation;
import com.navimee.places.googleGeocoding.enums.GeoType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.navimee.places.googleGeocoding.GoogleGeoTypeGetter.*;

public class Events {

    public static Map<String, List<Event>> sevenDaysSegregation(List<Event> events) {

        Map<String, List<Event>> segregated = new HashMap<>();

        for (EventsSegregation eventsSegregation : EventsSegregation.values()) {
            List<Event> filtered = events.stream().filter(eventsSegregation.getPredicate()).collect(Collectors.toList());
            segregated.put(eventsSegregation.toString(), filtered);
        }

        return segregated;
    }


    public static boolean complement(Event event, GooglePlace place, GooglePlace searchPlace) {

        if (event.searchPlace.id.equals(event.place.id)) {
            event.place = event.searchPlace;

            if(searchPlace != null && event.place.address == null || event.place.address.isEmpty()) {
                event.place.address = getType(searchPlace, GeoType.route) + " " + getType(searchPlace, GeoType.street_number);
                event.place.city = getType(searchPlace, GeoType.administrative_area_level_1);
            }

            return true;
        }

        if (place == null)
            return false;

        if (similar(event.place.lat, place.geometry.lat) && similar(event.place.lon, place.geometry.lon)) {
            event.place.city = getType(place, GeoType.administrative_area_level_1);
            event.place.address = getType(place, GeoType.route) + " " + getType(place, GeoType.street_number);
            event.place.lat = place.geometry.lat;
            event.place.lon = place.geometry.lon;
        }

        return true;
    }

    private static boolean similar(double a, double b) {
        return similar(a, b, 5.96e-08);
    }

    private static boolean similar(double a, double b, double epsilon) {
        return Math.abs(a / b - 1) < epsilon;
    }
}
