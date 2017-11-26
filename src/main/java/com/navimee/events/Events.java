package com.navimee.events;

import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.models.entities.events.FbEvent;
import com.navimee.places.googleGeocoding.enums.GeoType;

import java.util.concurrent.Future;

import static com.navimee.places.googleGeocoding.GoogleGeoTypeGetter.getType;

public class Events {

    public static boolean complement(FbEvent event, Future<GooglePlaceDto> placeDto, Future<GooglePlaceDto> searchPlaceDto) {

        try {
            GooglePlaceDto searchPlace = searchPlaceDto.get();
            GooglePlaceDto place = placeDto.get();

            // A place from an event is the same as the search place.
            if (event.getPlace() != null
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean similar(double a, double b) {
        return similar(a, b, 0.5);
    }

    private static boolean similar(double a, double b, double epsilon) {
        return Math.abs(a / b - 1) < epsilon;
    }
}
