package com.navimee.events;

import com.navimee.contracts.models.events.Event;
import com.navimee.contracts.models.places.GooglePlace;
import com.navimee.enums.EventsSegregation;
import com.navimee.places.googleGeocoding.enums.GeoType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.navimee.places.googleGeocoding.GoogleGeoTypeGetter.getType;

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

        // A place from an event is the same as the search place.
        if (event.searchPlace.id != null
                && event.place != null
                && event.place.id != null
                && event.searchPlace.id.equals(event.place.id)) {

            event.place = event.searchPlace;

            if (searchPlace != null
                    && event.place.address == null
                    || (event.place.address != null && event.place.address.isEmpty())) {

                event.place.city = getType(searchPlace, GeoType.administrative_area_level_1);
                event.place.address =
                        getType(searchPlace, GeoType.route) + " " + getType(searchPlace, GeoType.street_number);
            }

            return true;
        }

        // Ignore all places which do not have a lat and lon.
        if (place == null)
            return false;

        // A place is somewhere near to a searchPlace -> look at the similar function() the epsilon is equal 0.5
        if (similar(event.searchPlace.lat, place.geometry.lat) && similar(event.searchPlace.lon, place.geometry.lon)) {
            event.place.city = getType(place, GeoType.administrative_area_level_1);
            event.place.address = getType(place, GeoType.route) + " " + getType(place, GeoType.street_number);
            event.place.lat = place.geometry.lat;
            event.place.lon = place.geometry.lon;
        }

        return true;
    }

    private static boolean similar(double a, double b) {
        return similar(a, b, 0.5);
    }

    private static boolean similar(double a, double b, double epsilon) {
        return Math.abs(a / b - 1) < epsilon;
    }
}
