package com.navimee.contracts.repositories.palces;

import com.navimee.contracts.models.firestore.City;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.Place;

import java.util.List;
import java.util.Map;

public interface PlacesRepository {

    String coordinatesPath = "coordinates";
    String availableCitiesPath = "availableCities";
    String placesPath = "places";

    List<Coordinate> getCoordinates(String city);

    List<City> getAvailableCities();

    <T extends Place> List<T> getPlaces(String city, Class<T> type);

    void setCoordinates(Map<String, List<Coordinate>> coordinatesMap);

    void setAvailableCities(List<City> cities);

    void setPlaces(List<Place> places, String city);
}
