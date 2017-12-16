package com.navimee.tasks;

import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.PlacesService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class DetailsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    public void executeDetailsTask() throws ExecutionException, InterruptedException {
        Logger.LOG(new Log(LogEnum.TASK, "Foursquare details update"));

        for (City city : placesRepository.getAvailableCities()) {
            if(city.getName().equals("WARSZAWA")){
                placesService.saveFoursquarePlacesDetails(city.getName()).get();
                Thread.sleep(1000 * 60 * 75);
            }
        }
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    public void task() {
        try {
            this.executeDetailsTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
