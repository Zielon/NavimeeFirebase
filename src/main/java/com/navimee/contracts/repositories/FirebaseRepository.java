package com.navimee.contracts.repositories;

import com.navimee.models.entities.events.FbEvent;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;

import java.util.List;
import java.util.concurrent.Future;

public interface FirebaseRepository {
    Future transferEvents(List<FbEvent> events);

    Future transferPlaces(List<FsPlaceDetails> placeDetails);

    Future deleteEvents(List<FbEvent> events);
}
