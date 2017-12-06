package com.navimee.contracts.services;

import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.models.entities.coordinates.Coordinate;

import java.util.concurrent.Future;

public interface PlacesService {
    Future saveFacebookPlaces(String city);

    Future saveFoursquarePlaces(String city);

    Future saveFoursquarePlacesDetails(String city);

    Future<GooglePlaceDto> downloadReverseGeocoding(Coordinate coordinate);
}
