package com.navimee.contracts.repositories.palces;

import com.navimee.contracts.models.dataTransferObjects.firestore.CityDto;
import com.navimee.contracts.models.dataTransferObjects.placeDetails.FoursquarePlaceDetailsDto;
import com.navimee.contracts.models.dataTransferObjects.places.PlaceDto;
import com.navimee.contracts.models.dataTransferObjects.places.subelement.CoordinateDto;

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

    List<CoordinateDto> getCoordinates(String city);

    List<CityDto> getAvailableCities();

    <T extends PlaceDto> List<T> getPlaces(String city, Class<T> type);

    <T extends PlaceDto> List<T> getFoursquarePlaces(String city, Class<T> type);

    List<FoursquarePlaceDetailsDto> getFoursquarePlacesDetails(String city);

    Future setCoordinates(Map<String, List<CoordinateDto>> coordinatesMap);

    Future setAvailableCities(List<CityDto> cities);

    Future setPlaces(List<? extends PlaceDto> places, String city);

    Future setFoursquarePlaces(List<? extends PlaceDto> places, String city);

    Future setFoursquarePlacesDetails(List<FoursquarePlaceDetailsDto> details, String city);

    Future deleteCollection(String collection);
}
