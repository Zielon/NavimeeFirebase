package com.navimee.tasks.places;

import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.City;
import com.navimee.models.entities.coordinates.Coordinate;
import com.navimee.staticData.NavimeeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class PlacesTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

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

        Logger.LOG(new Log(LogEnum.TASK, "Places update", 0));

        placesRepository.getAvailableCities().forEach(city -> {
                    placesService.saveFoursquarePlaces(city.getName());
                    placesService.saveFacebookPlaces(city.getName());
                }
        );
    }

    //Once per 30 days.
    @Scheduled(cron = "0 00 12 ? * *")
    public void task() throws ExecutionException, InterruptedException {
        this.addPlacesTask();
    }
}
