package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import com.navimee.contracts.services.places.PlacesDetailsService;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
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
                try {
                    placesService.savePlacesDetails(city.getId()).join();
                } catch (Exception e) {
                    Logger.LOG(new Log(LogTypes.EXCEPTION, e));
                }
            }
        });
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    public void task() {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executeDetailsTask();
    }
}
