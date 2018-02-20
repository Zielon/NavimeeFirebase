package com.navimee.contracts.repositories;

import com.navimee.models.entities.Event;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventsRepository {

    CompletableFuture<List<Event>> getEvents();

    CompletableFuture<List<Event>> getEventsBefore(int timeToEnd);

    CompletableFuture<Void> setEvents(List<? extends Event> events);

    CompletableFuture<Void> removeEvents();
}
