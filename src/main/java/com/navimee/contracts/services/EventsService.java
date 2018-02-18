package com.navimee.contracts.services;

import java.util.concurrent.CompletableFuture;

public interface EventsService {
    CompletableFuture<Void> saveFacebookEvents(String city);

    CompletableFuture<Void> savePredictHqEvents(String city);
}