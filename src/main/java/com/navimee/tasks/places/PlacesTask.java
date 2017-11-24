package com.navimee.tasks.places;

import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Component
public class PlacesTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    // Once per 30 days.
    //@Scheduled(cron = "0 00 12 ? * *")
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void addPlacesTask() throws ExecutionException, InterruptedException {

        // Mocked data.
        /*com.navimee.mockups.NavimeeData navimeeData = new com.navimee.mockups.NavimeeData();
        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> cities = navimeeData.getCities();*/

        //placesRepository.deleteCollection("availableCities").get();
        //placesRepository.deleteCollection("events").get();
        //placesRepository.deleteCollection("coordinates").get();

        //placesRepository.setCoordinates(coordinates).get();
        //placesRepository.setAvailableCities(cities).get();

        placesRepository.deleteCollection("places").get();
        placesRepository.deleteCollection("foursquarePlaces").get();

        placesRepository.getAvailableCities().forEach(city -> {
                    if (city.getName().equals("SOPOT"))
                        Executors.newSingleThreadExecutor().submit(() -> {
                            placesService.saveFoursquarePlaces(city.getName());
                            placesService.saveFacebookPlaces(city.getName());
                        });
                }
        );
    }
}
