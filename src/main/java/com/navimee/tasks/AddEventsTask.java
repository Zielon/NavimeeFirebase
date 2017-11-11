package com.navimee.tasks;

import com.navimee.contracts.models.events.Event;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.events.EventsService;
import com.navimee.firestoreHelpers.TransactionSplit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
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
    public void addEvents() throws ExecutionException, InterruptedException {

        List<Future> futures = new ArrayList<>();

        Map<String, Integer> map = new HashMap<>();

        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);
        map.put("5", 5);
        map.put("6", 5);
        map.put("7", 5);
        map.put("8", 5);
        map.put("9", 5);
        map.put("10", 5);
        map.put("11", 5);
        map.put("12", 5);
        map.put("13", 5);

        TransactionSplit.mapSplit(map, 13);


        placesRepository.getAvailableCities().parallelStream().forEach(city ->
                Executors.newSingleThreadExecutor().submit(() -> {
                            System.out.println("EVENT TASK STARTED " + city.name + " at " + new Date());
                            List<Place> places = placesRepository.getPlaces(city.name, Place.class);
                            List<Event> events = eventsService.getFacebookEvents(places);
                            System.out.println("EVENT TASK ENDED " + city.name + " at " + new Date());
                            futures.add(eventsRepository.updateEvents(events, city.name));
                        }
                ));
    }
}
