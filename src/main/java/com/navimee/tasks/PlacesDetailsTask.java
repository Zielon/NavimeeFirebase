package com.navimee.tasks;

import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.PlacesService;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class PlacesDetailsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    public void executeDetailsTask() {
        for (City city : placesRepository.getAvailableCities()) {
            try {
                placesService.saveFoursquarePlacesDetails(city.getName()).get();
            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            }
        }
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    public void task() throws InterruptedException, ExecutionException {
        this.executeDetailsTask();
    }
}
