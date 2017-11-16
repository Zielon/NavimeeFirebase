package com.navimee.contracts.repositories.palces;

import com.navimee.contracts.models.firestore.City;
import com.navimee.contracts.models.placeDetails.FoursquarePlaceDetails;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.Place;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface PlacesRepository {

    String coordinatesPath = "coordinates";
    String availableCitiesPath = "availableCities";
    String placesPath = "places";
    String placesChunks = "placesChunks";
    String foursquarePlacesPath = "foursquarePlaces";
    String foursquarePlacesDetailsPath = "foursquarePlacesDetails";

    List<Coordinate> getCoordinates(String city);

    List<City> getAvailableCities();

    <T extends Place> List<T> getPlaces(String city, Class<T> type);

    <T extends Place> List<T> getFoursquarePlaces(String city, Class<T> type);

    Future setCoordinates(Map<String, List<Coordinate>> coordinatesMap);

    Future setAvailableCities(List<City> cities);

    Future setPlaces(List<? extends Place> places, String city);

    Future setFoursquarePlaces(List<? extends Place> places, String city);

    Future setFoursquarePlacesDetails(List<FoursquarePlaceDetails> details, String city);

    Future deleteCollection(String collection);
}
