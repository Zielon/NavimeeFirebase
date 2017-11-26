package com.navimee.contracts.repositories.palces;

import com.navimee.models.entities.general.City;
import com.navimee.models.entities.general.Coordinate;
import com.navimee.models.entities.places.Place;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.models.entities.places.foursquare.FsPlace;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface PlacesRepository {

    // GETTERS
    List<Coordinate> getCoordinates(String city);

    List<City> getAvailableCities();

    List<FbPlace> getFacebookPlaces(String city);

    List<FsPlace> getFoursquarePlaces(String city);

    List<FsPlaceDetails> getFoursquarePlacesDetails(String city);

    // SETTERS
    Future setCoordinates(Map<String, List<Coordinate>> coordinatesMap);

    Future addCoordinates(Coordinate coordinate, String city);

    Future setAvailableCities(List<City> cities);

    Future setFacebookPlaces(List<Place> places, String city);

    Future setFoursquarePlaces(List<Place> places, String city);

    Future setFoursquarePlacesDetails(List<FsPlaceDetails> details, String city);

    // DELETE
    Future deleteCollection(String collection);

    Future deleteCoordinates(String document, String city);
}
