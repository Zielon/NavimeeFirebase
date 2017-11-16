package com.navimee.tasks.places;

import com.navimee.contracts.models.placeDetails.FoursquarePlaceDetails;
import com.navimee.contracts.models.places.FoursquarePlace;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Component
public class DetailsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    @Scheduled(cron = "0 00 12 ? * *")
    //@Scheduled(fixedRate = 1000 * 60 * 60)
    public void addDetailsTask() throws ExecutionException, InterruptedException {

        placesRepository.getAvailableCities().forEach(city -> {
                    if (city.name.equals("SOPOT"))
                        Executors.newSingleThreadExecutor().submit(() -> {
                                    List<FoursquarePlace> foursquarePlaces = placesRepository.getFoursquarePlaces(city.name, FoursquarePlace.class);
                                    List<FoursquarePlaceDetails> details = placesService.getFoursquarePlacesDetails(foursquarePlaces);
                                    placesRepository.setFoursquarePlacesDetails(details, city.name);
                                }
                        );
                }
        );
    }
}
