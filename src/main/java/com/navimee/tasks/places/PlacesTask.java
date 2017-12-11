package com.navimee.tasks.places;

import com.navimee.contracts.repositories.FirestoreRepository;
import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.PlacesService;
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

@Component
public class PlacesTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    @Autowired
    FirestoreRepository firestoreRepository;

    public void addPlacesTask() {

        // Mocked data.
        NavimeeData navimeeData = new NavimeeData();
        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> cities = navimeeData.getCities();

/*              firestoreRepository.deleteCollection(AVAILABLE_CITIES_COLLECTION);
              placesRepository.setAvailableCities(cities).get();

              firestoreRepository.deleteCollection(COORDINATES_COLLECTION);
              firestoreRepository.deleteCollection(FOURSQUARE_PLACES_COLLECTION);
              firestoreRepository.deleteCollection(FACEBOOK_PLACES_COLLECTION);

              placesRepository.setCoordinates(coordinates).get();*/

        Logger.LOG(new Log(LogEnum.TASK, "Places update"));

        placesRepository.getAvailableCities().forEach(city -> {
                    placesService.saveFoursquarePlaces(city.getName());
                    placesService.saveFacebookPlaces(city.getName());
                }
        );
    }

    @Scheduled(cron = "0 0 0 1 ? *")
    public void task() {
        this.addPlacesTask();
    }
}
