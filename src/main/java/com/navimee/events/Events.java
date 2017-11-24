package com.navimee.events;

import com.navimee.enums.EventsSegregation;
import com.navimee.models.entities.events.FbEvent;
import com.navimee.models.externalDto.geocoding.GooglePlaceDto;
import com.navimee.places.googleGeocoding.enums.GeoType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.navimee.places.googleGeocoding.GoogleGeoTypeGetter.getType;
import static java.util.stream.Collectors.toList;

public class Events {

    public static Map<String, List<FbEvent>> sevenDaysSegregation(List<FbEvent> events) {

        Map<String, List<FbEvent>> segregated = new HashMap<>();

        for (EventsSegregation eventsSegregation : EventsSegregation.values()) {
            List<FbEvent> filtered = events.stream().filter(eventsSegregation.getPredicate()).collect(toList());
            segregated.put(eventsSegregation.toString(), filtered);
        }

        return segregated;
    }


    public static boolean complement(FbEvent event, GooglePlaceDto place, GooglePlaceDto searchPlace) {

        // A place from an event is the same as the search place.
        if (event.getSearchPlace().getId() != null
                && event.getPlace() != null
                && event.getPlace().getId() != null
                && event.getSearchPlace().getId().equals(event.getPlace().getId())) {

            event.setPlace(event.getSearchPlace());

            if (searchPlace != null
                    && event.getPlace().getAddress() == null
                    || (event.getPlace().getAddress() != null && event.getPlace().getAddress().isEmpty())) {

                event.getPlace().setCity(getType(searchPlace, GeoType.administrative_area_level_1));
                event.getPlace().setAddress(getType(searchPlace, GeoType.route) + " " + getType(searchPlace, GeoType.street_number));
            }

            return true;
        }

        // Ignore all places which do not have a lat and lon.
        if (place == null)
            return false;

        // A place is somewhere near to a searchPlace -> look at the similar function() the epsilon is equal 0.5
        if (similar(event.getSearchPlace().getLat(), place.geometry.lat) && similar(event.getSearchPlace().getLon(), place.geometry.lon)) {
            event.getPlace().setCity(getType(place, GeoType.administrative_area_level_1));
            event.getPlace().setAddress(getType(place, GeoType.route) + " " + getType(place, GeoType.street_number));
            event.getPlace().setLat(place.geometry.lat);
            event.getPlace().setLon(place.geometry.lon);
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
