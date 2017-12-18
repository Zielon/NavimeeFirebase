package com.navimee.events;

import com.google.cloud.firestore.GeoPoint;
import com.navimee.contracts.services.PlacesService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.bo.FbEvent;
import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.models.entities.Event;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.Coordinate;
import com.navimee.models.entities.places.Place;
import com.navimee.places.googleGeocoding.enums.GeoType;

import java.util.concurrent.Future;
import java.util.function.Function;

import static com.navimee.places.googleGeocoding.GoogleGeoTypeGetter.getType;

public class EventsHelpers {

    public static Function<FbEvent, Boolean> getCompelmentFunction(PlacesService placesService) {
        return event -> {
            if (event.getPlace() == null || event.getPlace().getGeoPoint() == null) return false;
            if (event.getSearchPlace() == null || event.getSearchPlace().getGeoPoint() == null) return false;

            Future<GooglePlaceDto> place = placesService.
                    downloadReverseGeocoding(new Coordinate(
                            event.getPlace().getGeoPoint().getLatitude(),
                            event.getPlace().getGeoPoint().getLongitude()));

            Future<GooglePlaceDto> searchPlace = placesService.
                    downloadReverseGeocoding(new Coordinate(
                            event.getSearchPlace().getGeoPoint().getLatitude(),
                            event.getSearchPlace().getGeoPoint().getLongitude()));

            return complement(event, place, searchPlace);
        };
    }

    public static Function<Event, Event> setAddress(PlacesService placesService) {
        return event -> {
            try {
                GooglePlaceDto googlePlace = placesService.downloadReverseGeocoding(new Coordinate(event.getGeoPoint().getLatitude(), event.getGeoPoint().getLongitude())).get();
                Place place = new Place();

                place.setCity(getType(googlePlace, GeoType.administrative_area_level_2));
                place.setAddress(getType(googlePlace, GeoType.route) + " " + getType(googlePlace, GeoType.street_number));
                place.setId("");
                place.setName("");

                event.setPlace(place);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return event;
        };
    }

    private static boolean complement(FbEvent event, Future<GooglePlaceDto> placeDto, Future<GooglePlaceDto> searchPlaceDto) {

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

                    event.getPlace().setCity(getType(searchPlace, GeoType.administrative_area_level_2));
                    event.getPlace().setAddress(getType(searchPlace, GeoType.route) + " " + getType(searchPlace, GeoType.street_number));
                }

                return true;
            }

            // Ignore all places which do not have a lat and lon.
            if (place == null)
                return false;

            // A place is somewhere near to a searchPlace -> look at the similar function() the epsilon is equal 0.5
            if (similar(event.getSearchPlace().getGeoPoint().getLatitude(), place.geometry.lat)
                    && similar(event.getSearchPlace().getGeoPoint().getLongitude(), place.geometry.lon)) {

                event.getPlace().setCity(getType(place, GeoType.administrative_area_level_2));
                event.getPlace().setAddress(getType(place, GeoType.route) + " " + getType(place, GeoType.street_number));
                event.getPlace().setGeoPoint(new GeoPoint(place.geometry.lat, place.geometry.lon));

                return true;
            }

        } catch (Exception e) {
            Logger.LOG(new Log(LogEnum.EXCEPTION, e));
        }

        return false;
    }

    private static boolean similar(double a, double b) {
        return similar(a, b, 0.25);
    }

    private static boolean similar(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }
}
