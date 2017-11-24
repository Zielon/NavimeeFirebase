package com.navimee.contracts.repositories.palces;

import com.navimee.models.entities.general.City;
import com.navimee.models.entities.general.Coordinate;
import com.navimee.models.entities.places.FsPlaceDetails;
import com.navimee.models.entities.places.Place;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface PlacesRepository {

    // FIRESTORE PATHS
    String coordinatesPath = "coordinates";
    String availableCitiesPath = "availableCities";
    String placesPath = "places";
    String foursquarePlacesPath = "foursquarePlaces";
    String foursquarePlacesDetailsPath = "foursquarePlacesDetails";

    // GETTERS
    List<Coordinate> getCoordinates(String city);

    List<City> getAvailableCities();

    List<Place> getPlaces(String city);

    List<Place> getFoursquarePlaces(String city);

    List<FsPlaceDetails> getFoursquarePlacesDetails(String city);

    // SETTERS
    Future setCoordinates(Map<String, List<Coordinate>> coordinatesMap);

    Future setAvailableCities(List<City> cities);

    Future setPlaces(List<Place> places, String city);

    Future setFoursquarePlaces(List<Place> places, String city);

    Future setFoursquarePlacesDetails(List<FsPlaceDetails> details, String city);

    // DELETE
    Future deleteCollection(String collection);
}
