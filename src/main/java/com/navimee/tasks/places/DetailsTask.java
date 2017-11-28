package com.navimee.tasks.places;

import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class DetailsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    //@Scheduled(fixedRate = 1000 * 60 * 60)
    public void addDetailsTask() throws ExecutionException, InterruptedException {
        placesRepository.getAvailableCities().forEach(city -> {
                    //if (city.getName().equals("SOPOT"))
                    placesService.saveFoursquarePlacesDetails(city.getName());
                }
        );
    }
}
