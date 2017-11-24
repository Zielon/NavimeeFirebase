package com.navimee.contracts.services.places;

import com.navimee.models.entities.general.Coordinate;
import com.navimee.models.externalDto.geocoding.GooglePlaceDto;

public interface PlacesService {
    void saveFacebookPlaces(String city);

    void saveFoursquarePlaces(String city);

    void saveFoursquarePlacesDetails(String city);

    GooglePlaceDto downloadReverseGeocoding(Coordinate coordinate);
}
