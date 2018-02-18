package com.navimee.contracts.services.places;

import java.util.concurrent.CompletableFuture;

public interface PlacesService {

    CompletableFuture<Void> savePlaces(String city);
}
