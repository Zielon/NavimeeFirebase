package com.navimee.tasks.places;

import com.navimee.contracts.models.dataTransferObjects.placeDetails.FoursquarePlaceDetailsDto;
import com.navimee.contracts.models.dataTransferObjects.places.FoursquarePlaceDto;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import org.springframework.beans.factory.annotation.Autowired;
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

    //@Scheduled(fixedRate = 1000 * 60 * 60)
    public void addDetailsTask() throws ExecutionException, InterruptedException {

        placesRepository.getAvailableCities().forEach(city -> {
                    if (city.name.equals("GDANSK"))
                        Executors.newSingleThreadExecutor().submit(() -> {
                                    List<FoursquarePlaceDto> foursquarePlaces = placesRepository.getFoursquarePlaces(city.name, FoursquarePlaceDto.class);
                                    List<FoursquarePlaceDetailsDto> details = placesService.getFoursquarePlacesDetails(foursquarePlaces);
                                    placesRepository.setFoursquarePlacesDetails(details, city.name);
                                }
                        );
                }
        );
    }
}
