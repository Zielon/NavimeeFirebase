package com.navimee.contracts.services;

import com.navimee.models.entities.places.facebook.FbPlace;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventsService {

    CompletableFuture<Void> saveFacebookEvents(List<FbPlace> places, boolean complement);

    CompletableFuture<Void> savePredictHqEvents(String city);
}