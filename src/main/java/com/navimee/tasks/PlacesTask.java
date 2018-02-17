package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.FirestoreRepository;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import com.navimee.contracts.services.PlacesService;
import com.navimee.logger.LogTypes;
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

import static com.navimee.firestore.FirebasePaths.*;

@Component
public class PlacesTask {

    @Autowired
    CoordinatesRepository coordinatesRepository;

    @Autowired
    PlacesService placesService;

    @Autowired
    FirestoreRepository firestoreRepository;

    public void executePlacesTask() throws ExecutionException, InterruptedException {
        // Mocked data.
        NavimeeData navimeeData = new NavimeeData();
        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> staticData = navimeeData.getCities();

        firestoreRepository.deleteCollection(AVAILABLE_CITIES);
        coordinatesRepository.setAvailableCities(staticData).join();

        firestoreRepository.deleteCollection(COORDINATES);
        firestoreRepository.deleteCollection(FOURSQUARE_PLACES);
        firestoreRepository.deleteCollection(FACEBOOK_PLACES);

        coordinatesRepository.setCoordinates(coordinates).join();

        coordinatesRepository.getAvailableCities().thenAcceptAsync(cities -> {
            for (City city : cities) {
                try {
                    placesService.saveFacebookPlaces(city.getName()).get();
                    placesService.saveFoursquarePlaces(city.getName()).get();
                } catch (Exception e) {
                    Logger.LOG(new Log(LogTypes.EXCEPTION, e));
                }
            }
        });
    }

    @Scheduled(cron = "0 0 1 2 * ?")
    public void task() throws ExecutionException, InterruptedException {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executePlacesTask();
    }
}
