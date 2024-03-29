package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import com.navimee.contracts.services.places.PlacesDetailsService;
import com.navimee.models.entities.coordinates.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PlacesDetailsTask {

    @Autowired
    CoordinatesRepository coordinatesRepository;

    @Autowired
    PlacesDetailsService placesService;

    public void executeDetailsTask() {
        coordinatesRepository.getAvailableCities().thenAcceptAsync(cities -> {
            for (City city : cities) {
                placesService.savePlacesDetails(city.getId());
            }
        });
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    public void task() {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executeDetailsTask();
    }
}
