package com.navimee.contracts.repositories;

import com.firebase.geofire.GeoLocation;
import com.navimee.models.entities.Entity;
import com.navimee.models.entities.HotspotEvent;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FirebaseRepository {
    Future transferEvents(List<HotspotEvent> events);

    Future transferPlaces(List<FsPlaceDetails> placeDetails);

    void deleteCurrentHotspot();

    Future deleteEvents(List<HotspotEvent> events);

    <T extends Entity> Future filterAndTransfer(List<T> entities, Predicate<T> predicate, Function<T, GeoLocation> function);
}