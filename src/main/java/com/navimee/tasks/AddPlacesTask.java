package com.navimee.tasks;

import com.navimee.contracts.models.firestore.City;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.mockups.NavimeeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class AddPlacesTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    // Once per 30 days.
    @Scheduled(fixedRate = 1000 * 60 * 24 * 30)
    public void addPlaces() throws ExecutionException, InterruptedException {

        // Mocked data.
        NavimeeData navimeeData = new NavimeeData();
        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> cities = navimeeData.getCities();

        placesRepository.setCoordinates(coordinates).get();
        placesRepository.setAvailableCities(cities).get();

        ExecutorService executor = Executors.newFixedThreadPool(15);

        List<Future> futures = new ArrayList<>();

        placesRepository.getAvailableCities().forEach(
                city -> futures.add(executor.submit(() -> {
                            List<Place> palces = placesService.getFacebookPlaces(placesRepository.getCoordinates(city.name));
                            return placesRepository.setPlaces(palces, city.name);
                        }
                )));

        for (Future f : futures) f.get();
    }
}
