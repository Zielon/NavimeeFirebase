package com.navimee.queries.events;

import com.navimee.contracts.services.places.GeoService;
import com.navimee.models.dto.events.FbEventDto;
import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.models.entities.coordinates.Coordinate;
import com.navimee.queries.places.googleGeocoding.enums.GeoType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.navimee.asyncCollectors.Completable.sequence;
import static com.navimee.queries.places.googleGeocoding.GoogleGeoTypeGetter.getType;

public class EventsHelpers {

    public static Function<FbEventDto, Boolean> getCompelmentFunction(GeoService<GooglePlaceDto> geoService) {
        return event -> {
            if (event.getPlace() == null || event.getSearchPlace() == null) return false;
            return complement(geoService, event);
        };
    }

    private static boolean complement(GeoService<GooglePlaceDto> geoService, FbEventDto event) {

        List<CompletableFuture<GooglePlaceDto>> list = new ArrayList<>();

        list.add(geoService.geocodingCoordinate(new Coordinate(event.getPlace().getLat(), event.getPlace().getLon())));
        list.add(geoService.geocodingCoordinate(new Coordinate(event.getSearchPlace().getLat(), event.getSearchPlace().getLon())));

        return sequence(list).thenApplyAsync(googlePlaceDtos -> {

            GooglePlaceDto place = googlePlaceDtos.get(0);
            GooglePlaceDto searchPlace = googlePlaceDtos.get(1);

            if(searchPlace == null){
                String address = event.getSearchPlace().getFullAddress();
                if(!address.equals(""))
                    searchPlace = geoService.geocodingAddress(address).join();
            }

            if(place == null){
                String address = event.getPlace().getFullAddress();
                if(!address.equals(""))
                    searchPlace = geoService.geocodingAddress(address).join();
            }

            // A place from an event is the same as the search place.
            if (event.getSearchPlace().getId().equals(event.getPlace().getId())) {

                if (event.getPlace().getAddress() == null) {
                    event.getPlace().setCity(getType(searchPlace, GeoType.administrative_area_level_2));
                    event.getPlace().setAddress(getType(searchPlace, GeoType.route) + " " + getType(searchPlace, GeoType.street_number));
                }

                return true;
            }

            // Ignore all places which do not have a lat and lon.
            if (place == null)
                return false;

            // A place is somewhere near to a searchPlace -> look at the similar function() the epsilon is equal 0.5
            if (similar(event.getSearchPlace().getLat(), place.geometry.lat)
                && similar(event.getSearchPlace().getLon(), place.geometry.lon)) {

                event.getPlace().setCity(getType(place, GeoType.administrative_area_level_2));
                event.getPlace().setAddress(getType(place, GeoType.route) + " " + getType(place, GeoType.street_number));
                event.getPlace().setLat(place.geometry.lat);
                event.getPlace().setLat(place.geometry.lon);

                return true;
            }

            return false;

        }).join();
    }

    private static boolean similar(double a, double b) {
        return similar(a, b, 0.25);
    }

    private static boolean similar(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }
}
