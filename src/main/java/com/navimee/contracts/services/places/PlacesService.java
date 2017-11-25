package com.navimee.contracts.services.places;

import com.navimee.models.entities.general.Coordinate;
import com.navimee.models.dto.geocoding.GooglePlaceDto;

import java.util.concurrent.Future;

public interface PlacesService {
    Future saveFacebookPlaces(String city);

    Future saveFoursquarePlaces(String city);

    Future saveFoursquarePlacesDetails(String city);

    GooglePlaceDto downloadReverseGeocoding(Coordinate coordinate);
}
