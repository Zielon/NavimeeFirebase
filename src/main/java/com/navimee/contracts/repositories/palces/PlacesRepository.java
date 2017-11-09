package com.navimee.contracts.repositories.palces;

import com.navimee.contracts.models.firestore.City;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.Place;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface PlacesRepository {

    String coordinatesPath = "coordinates";
    String availableCitiesPath = "availableCities";
    String placesPath = "places";

    List<Coordinate> getCoordinates(String city);

    List<City> getAvailableCities();

    <T extends Place> List<T> getPlaces(String city, Class<T> type);

    Future setCoordinates(Map<String, List<Coordinate>> coordinatesMap);

    Future setAvailableCities(List<City> cities);

    Future setPlaces(List<Place> places, String city);
}
