package com.navimee.contracts.repositories;

import com.firebase.geofire.GeoLocation;
import com.navimee.models.entities.Entity;
import com.navimee.models.entities.events.FbEvent;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FirebaseRepository {
    Future transferEvents(List<FbEvent> events);

    Future transferPlaces(List<FsPlaceDetails> placeDetails);

    void deleteCurrentHotspot();

    Future deleteEvents(List<FbEvent> events);

    <T extends Entity> Future filterAndTransferToCurrent(List<T> entities, Predicate<T> predicate, Function<T, GeoLocation> function);
}