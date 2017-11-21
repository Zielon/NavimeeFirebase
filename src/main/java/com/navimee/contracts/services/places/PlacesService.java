package com.navimee.contracts.services.places;

import com.navimee.contracts.models.dataTransferObjects.placeDetails.FoursquarePlaceDetailsDto;
import com.navimee.contracts.models.dataTransferObjects.places.FacebookPlaceDto;
import com.navimee.contracts.models.dataTransferObjects.places.FoursquarePlaceDto;
import com.navimee.contracts.models.dataTransferObjects.places.GooglePlaceDto;
import com.navimee.contracts.models.dataTransferObjects.places.subelement.CoordinateDto;

import java.util.List;

public interface PlacesService {
    List<FacebookPlaceDto> getFacebookPlaces(List<CoordinateDto> coordinates);

    List<FoursquarePlaceDto> getFoursquarePlaces(List<CoordinateDto> coordinates);

    List<FoursquarePlaceDetailsDto> getFoursquarePlacesDetails(List<FoursquarePlaceDto> places);

    GooglePlaceDto getReverseGeocoding(CoordinateDto coordinate);
}
