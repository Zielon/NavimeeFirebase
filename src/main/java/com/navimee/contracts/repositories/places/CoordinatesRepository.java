package com.navimee.contracts.repositories.places;

import com.navimee.models.entities.coordinates.City;
import com.navimee.models.entities.coordinates.Coordinate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CoordinatesRepository {
    CompletableFuture<List<Void>> setCoordinates(Map<String, List<Coordinate>> coordinatesMap);

    CompletableFuture<List<Coordinate>> getCoordinates(String city);

    CompletableFuture<List<City>> getAvailableCities();

    CompletableFuture<List<Void>> setAvailableCities(List<City> cities);
}
