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
import java.util.concurrent.ExecutorService;

import static com.navimee.firestore.FirebasePaths.*;

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

    @Autowired
    ExecutorService executorService;

    public void executePlacesTask() throws ExecutionException, InterruptedException {
        NavimeeData navimeeData = new NavimeeData();
        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> staticData = navimeeData.getCities();

        firestoreRepository.deleteDocument(new PathBuilder().add(AVAILABLE_CITIES).addCountry().build()).join();

        CompletableFuture<Void> allDone = CompletableFuture.allOf(
                firestoreRepository.deleteDocument(new PathBuilder().add(FACEBOOK_PLACES).addCountry().build()),
                firestoreRepository.deleteDocument(new PathBuilder().add(FOURSQUARE_PLACES).addCountry().build()),
                firestoreRepository.deleteDocument(new PathBuilder().add(COORDINATES).addCountry().build()))
                .thenRunAsync(() -> {
                    coordinatesRepository.setCoordinates(coordinates).join();
                    coordinatesRepository.setAvailableCities(staticData).join();
                }).exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });

        allDone.thenAcceptAsync(results -> {
            coordinatesRepository.getAvailableCities().thenAcceptAsync(cities -> {
                for (City city : cities) {
                    try {
                        facebookService.savePlaces(city.getId());
                        foursquareService.savePlaces(city.getId());
                    } catch (Exception e) {
                        Logger.LOG(new Log(LogTypes.EXCEPTION, e));
                    }
                }
            });
        }, executorService);
    }

    @Scheduled(cron = "0 0 1 2 * ?")
    public void task() throws ExecutionException, InterruptedException {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executePlacesTask();
    }
}
