package com.navimee.tasks;

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

    public void executeDetailsTask() {
        Logger.LOG(new Log(LogEnum.TASK, "Foursquare details update"));

        placesRepository.getAvailableCities().forEach(city -> {
                    placesService.saveFoursquarePlacesDetails(city.getName());
                }
        );
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    public void task() {
        this.executeDetailsTask();
    }
}
