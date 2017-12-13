package com.navimee.tasks;

import com.firebase.geofire.GeoLocation;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.linq.HotspotFilters;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class HotspotTask {

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    FirebaseRepository firebaseRepository;

    @Autowired
    PlacesRepository placesRepository;

    public void addHotspotTask() throws ExecutionException, InterruptedException {

        Logger.LOG(new Log(LogEnum.TASK, "Hotspot update"));

        firebaseRepository.deleteCurrentHotspot();

        firebaseRepository.filterAndTransferToCurrent(
                placesRepository.getFoursquarePlacesDetails(),
                fsPlaceDetails -> true, //HotspotFilters.filterFsPopular(),
                fsPlaceDetails -> new GeoLocation(fsPlaceDetails.getLocationLat(), fsPlaceDetails.getLocationLng())).get();

        firebaseRepository.filterAndTransferToCurrent(
                eventsRepository.getEventsBeforeEnd(500),
                event -> true,
                event -> new GeoLocation(event.getPlace().getGeoPoint().getLatitude(), event.getPlace().getGeoPoint().getLongitude())).get();
    }

    @Scheduled(cron = "0 15 0 * * ?")
    public void task() throws ExecutionException, InterruptedException {
        this.addHotspotTask();
    }
}
