package com.navimee.tasks.places;

import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.models.entities.general.City;
import com.navimee.models.entities.general.Coordinate;
import com.navimee.staticData.NavimeeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static com.navimee.firestore.Paths.FACEBOOK_PLACES_COLLECTION;

@Component
public class PlacesTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    @Autowired
    ExecutorService executorService;

    //Once per 30 days.
    //@Scheduled(cron = "0 00 12 ? * *")
    //@Scheduled(fixedRate = 1000 * 60 * 60)
    public void addPlacesTask() throws ExecutionException, InterruptedException {

        // Mocked data.
        NavimeeData navimeeData = new NavimeeData();
        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> cities = navimeeData.getCities();

        // placesRepository.deleteCollection(COORDINATES_COLLECTION).get();
        // placesRepository.deleteCollection(FOURSQUARE_PLACES_COLLECTION).get();
        // placesRepository.deleteCollection(FACEBOOK_PLACES_COLLECTION).get();

        // placesRepository.setCoordinates(coordinates).get();
        // placesRepository.setAvailableCities(cities).get();

        placesRepository.getAvailableCities().forEach(city -> {
                    //if (city.getName().equals("SOPOT")){
                        placesService.saveFoursquarePlaces(city.getName());
                        placesService.saveFacebookPlaces(city.getName());
                    //};
                }
        );
    }
}
