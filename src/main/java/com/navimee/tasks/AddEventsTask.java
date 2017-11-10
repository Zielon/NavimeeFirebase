package com.navimee.tasks;

import com.navimee.contracts.models.events.Event;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.events.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Component
public class AddEventsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    @Autowired
    EventsRepository eventsRepository;

    // Once per 1 hour.
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void addPlaces() throws ExecutionException, InterruptedException {

        List<Future> futures = new ArrayList<>();

        placesRepository.getAvailableCities().parallelStream().forEach(city ->
                Executors.newSingleThreadExecutor().submit(() -> {
                        List<Place> places = placesRepository.getPlaces(city.name, Place.class);
                        List<Event> events = eventsService.getFacebookEvents(places);
                        futures.add(eventsRepository.updateEvents(events, city.name));
                    }
                ));
    }
}
