package com.navimee.contracts.services.places;

import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.FacebookPlace;
import com.navimee.contracts.models.places.FoursquareHotPlace;
import com.navimee.contracts.models.places.FoursquarePlace;

import java.util.List;

public interface PlacesService {
    List<FacebookPlace> getFacebookPlaces(List<Coordinate> coordinates);

    List<FoursquarePlace> getFoursquarePlaces(List<Coordinate> coordinates);

    List<FoursquareHotPlace> getFoursquareHotPlaces(List<Coordinate> coordinates);
}
