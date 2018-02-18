package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.configuration.Qualifiers;
import com.navimee.contracts.repositories.FirestoreRepository;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.firestore.PathBuilder;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.City;
import com.navimee.models.entities.coordinates.Coordinate;
import com.navimee.staticData.NavimeeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.navimee.firestore.FirebasePaths.AVAILABLE_CITIES;
import static com.navimee.firestore.FirebasePaths.FACEBOOK_PLACES;

@Component
public class PlacesTask {

    @Autowired
    CoordinatesRepository coordinatesRepository;

    @Autowired
    @Qualifier(Qualifiers.FACEBOOK)
    PlacesService facebookService;

    @Autowired
    @Qualifier(Qualifiers.FOURSQUARE)
    PlacesService foursquareService;

    @Autowired
    FirestoreRepository firestoreRepository;

    public void executePlacesTask() throws ExecutionException, InterruptedException {
        // Mocked data.
        NavimeeData navimeeData = new NavimeeData();
        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> staticData = navimeeData.getCities();

        firestoreRepository.deleteDocument(new PathBuilder().add(AVAILABLE_CITIES).addCountry().build()).join();

        CompletableFuture<Void> allDone = CompletableFuture.allOf(
                coordinatesRepository.setAvailableCities(staticData),
                firestoreRepository.deleteDocument(new PathBuilder().add(FACEBOOK_PLACES).addCountry().build()),
/*                firestoreRepository.deleteDocument(new PathBuilder().add(FOURSQUARE_PLACES).addCountry().build()),
                firestoreRepository.deleteDocument(new PathBuilder().add(COORDINATES).addCountry().build()),*/
                coordinatesRepository.setCoordinates(coordinates));

        allDone.thenAcceptAsync(results -> {
            coordinatesRepository.getAvailableCities().thenAcceptAsync(cities -> {
                for (City city : cities) {
                    if (city.getName().equals("SOPOT") || city.getName().equals("GDYNIA"))
                        try {
                            facebookService.savePlaces(city.getName()).join();
                            //foursquareService.savePlaces(city.getName()).join();
                        } catch (Exception e) {
                            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
                        }
                }
            });
        });
    }

    @Scheduled(cron = "0 0 1 2 * ?")
    public void task() throws ExecutionException, InterruptedException {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executePlacesTask();
    }
}
