package com.navimee.contracts.services.places;

import com.navimee.models.entities.coordinates.Coordinate;

import java.util.concurrent.CompletableFuture;

public interface GeoService<T> {

    CompletableFuture<T> downloadReverseGeocoding(Coordinate coordinate);
}
