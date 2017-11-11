package com.navimee.tasks;

import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Component
public class PlacesTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    // Once per 30 days.
    @Scheduled(cron = "0 00 12 ? * *")
    public void addPlacesTask() throws ExecutionException, InterruptedException {

        // Mocked data.
/*        com.navimee.mockups.NavimeeData navimeeData = new com.navimee.mockups.NavimeeData();
        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> cities = navimeeData.getCities();

        placesRepository.deleteCollection("places").get();
        placesRepository.deleteCollection("availableCities").get();
        placesRepository.deleteCollection("events").get();
        placesRepository.deleteCollection("coordinates").get();

        placesRepository.setCoordinates(coordinates).get();
        placesRepository.setAvailableCities(cities).get();*/

        placesRepository.getAvailableCities().forEach(city -> {
                    String name = city.name;
                    Executors.newSingleThreadExecutor().submit(() -> {
                        List<Place> places = placesService.getFacebookPlaces(placesRepository.getCoordinates(name));
                        placesRepository.setPlaces(places, name);
                    });
                }
        );
    }
}
