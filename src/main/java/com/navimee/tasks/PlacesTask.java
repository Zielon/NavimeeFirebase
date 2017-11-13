package com.navimee.tasks;

import com.navimee.contracts.models.places.FacebookPlace;
import com.navimee.contracts.models.places.FoursquarePlace;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void addPlacesTask() throws ExecutionException, InterruptedException {

        // Mocked data.
/*        com.navimee.mockups.NavimeeData navimeeData = new com.navimee.mockups.NavimeeData();
        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> cities = navimeeData.getCities();

        placesRepository.deleteCollection("availableCities").get();
        placesRepository.deleteCollection("events").get();
        placesRepository.deleteCollection("coordinates").get();

        placesRepository.setCoordinates(coordinates).get();
        placesRepository.setAvailableCities(cities).get();*/

        placesRepository.getAvailableCities().forEach(city -> {
                    String name = city.name;
                    Executors.newSingleThreadExecutor().submit(() -> {
                        List<FacebookPlace> facebookPlaces = placesService.getFacebookPlaces(placesRepository.getCoordinates(name));
                        List<FoursquarePlace> foursquarePlaces = placesService.getFoursquarePlaces(placesRepository.getCoordinates(name));

                        List<Place> places = new ArrayList<>();
                        places.addAll(facebookPlaces);
                        places.addAll(foursquarePlaces);

                        placesRepository.setPlaces(places, name);
                    });
                }
        );
    }
}
