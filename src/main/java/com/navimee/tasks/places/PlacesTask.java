package com.navimee.tasks.places;

import com.navimee.contracts.models.dataTransferObjects.firestore.CityDto;
import com.navimee.contracts.models.dataTransferObjects.places.FacebookPlaceDto;
import com.navimee.contracts.models.dataTransferObjects.places.FoursquarePlaceDto;
import com.navimee.contracts.models.dataTransferObjects.places.PlaceDto;
import com.navimee.contracts.models.dataTransferObjects.places.subelement.CoordinateDto;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
        com.navimee.mockups.NavimeeData navimeeData = new com.navimee.mockups.NavimeeData();
        Map<String, List<CoordinateDto>> coordinates = navimeeData.getCoordinates();
        List<CityDto> cities = navimeeData.getCities();

        //placesRepository.deleteCollection("availableCities").get();
        //placesRepository.deleteCollection("events").get();
        //placesRepository.deleteCollection("coordinates").get();

        //placesRepository.setCoordinates(coordinates).get();
        //placesRepository.setAvailableCities(cities).get();

        placesRepository.deleteCollection("places").get();
        placesRepository.deleteCollection("foursquarePlaces").get();

        placesRepository.getAvailableCities().forEach(city -> {
                    String name = city.name;
                    Executors.newSingleThreadExecutor().submit(() -> {
                        List<FacebookPlaceDto> facebookPlaces = placesService.getFacebookPlaces(placesRepository.getCoordinates(name));
                        List<FoursquarePlaceDto> foursquarePlaces = placesService.getFoursquarePlaces(placesRepository.getCoordinates(name));

                        List<PlaceDto> places = new ArrayList<>();
                        places.addAll(facebookPlaces);
                        places.addAll(foursquarePlaces.stream().filter(e -> e.facebook != null).collect(Collectors.toList()));

                        placesRepository.setFoursquarePlaces(foursquarePlaces, name);
                        placesRepository.setPlaces(places, name);
                    });
                }
        );
    }
}
