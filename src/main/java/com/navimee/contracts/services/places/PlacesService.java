package com.navimee.contracts.services.places;

import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.Place;

import java.util.List;

public interface PlacesService {
    List<Place> getFacebookPlaces(List<Coordinate> coordinates);

    List<Place> getFoursquarePlaces(List<Coordinate> coordinates);
}
