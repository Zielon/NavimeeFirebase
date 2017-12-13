package com.navimee.tasks.places;

import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.PlacesService;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DetailsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    public void addDetailsTask() {

        Logger.LOG(new Log(LogEnum.TASK, "Foursquare details update"));

        placesRepository.getAvailableCities().forEach(city -> {
                    try {
                        placesService.saveFoursquarePlacesDetails(city.getName()).get();
                        // The requests rate is 5000 per hour.
                        Thread.sleep(1000 * 60 * 60);
                    } catch (Exception e) {
                        Logger.LOG(new Log(LogEnum.EXCEPTION, e));
                    }
                }
        );
    }

    @Scheduled(cron = "0 0 10 1 * ?")
    public void task() {
        this.addDetailsTask();
    }
}
