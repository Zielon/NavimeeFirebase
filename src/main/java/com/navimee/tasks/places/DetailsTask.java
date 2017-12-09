package com.navimee.tasks.places;

import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.PlacesService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
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

    public void addDetailsTask() throws ExecutionException, InterruptedException {

        Logger.LOG(new Log(LogEnum.TASK, "Foursquare details update"));

        placesRepository.getAvailableCities().forEach(city -> {
                    //if (city.getName().equals("SOPOT"))
                    placesService.saveFoursquarePlacesDetails(city.getName());
                }
        );
    }

    // Once per 5 hour.
    @Scheduled(cron = "0 0 0/3 * * ?")
    public void task() {
        try {
            this.addDetailsTask();
        } catch (Exception e) {
            Logger.LOG(new Log(LogEnum.EXCEPTION, e));
        }
    }
}
