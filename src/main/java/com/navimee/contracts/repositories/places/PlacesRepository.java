package com.navimee.contracts.repositories.places;

import com.navimee.models.entities.places.Place;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PlacesRepository<T extends Place> {

    CompletableFuture<List<T>> getPlaces(String city);

    CompletableFuture<Void> setPlaces(List<T> places, String city);
}
