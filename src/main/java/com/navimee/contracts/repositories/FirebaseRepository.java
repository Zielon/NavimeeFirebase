package com.navimee.contracts.repositories;

import com.firebase.geofire.GeoLocation;
import com.navimee.models.entities.Event;
import com.navimee.models.entities.contracts.Entity;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FirebaseRepository {

    CompletableFuture<Void> transferEvents(List<Event> events);

    CompletableFuture<Void> transferPlaces(List<FsPlaceDetails> placeDetails);

    void deleteCurrentHotspot();

    CompletableFuture<Void> deleteEvents(List<Event> events);

    <T extends Entity> CompletableFuture<Void> filterAndTransfer(List<T> entities, Predicate<T> predicate, Function<T, GeoLocation> function);
}