package com.navimee.contracts.repositories.places;

import com.navimee.models.entities.places.Place;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PlacesDetailsRepository<TDetails, TPlace extends Place> extends PlacesRepository<TPlace> {

    CompletableFuture<Void> setPlacesDetails(List<TDetails> details);

    CompletableFuture<List<TDetails>> getPlacesDetails();
}
