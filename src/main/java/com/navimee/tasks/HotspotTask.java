package com.navimee.tasks;

import com.firebase.geofire.GeoLocation;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.linq.HotspotFilters;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

import static com.navimee.tasks.TasksFixedTimes.HOTSPOT;

@Component
public class HotspotTask {

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    FirebaseRepository firebaseRepository;

    @Autowired
    PlacesRepository placesRepository;

    public void executeHotspotTask() {
        try {
            firebaseRepository.deleteCurrentHotspot();

            firebaseRepository.filterAndTransfer(
                    placesRepository.getFoursquarePlacesDetails(),
                    HotspotFilters.filterFsPopular(),
                    fsPlaceDetails -> new GeoLocation(fsPlaceDetails.getLocationLat(), fsPlaceDetails.getLocationLng())).get();

            firebaseRepository.filterAndTransfer(
                    eventsRepository.getEventsBefore(60 * 2),
                    event -> true,
                    event -> new GeoLocation(event.getGeoPoint().getLatitude(), event.getGeoPoint().getLongitude())).get();

        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }
    }

    @Scheduled(fixedDelay = HOTSPOT)
    public void task() throws InterruptedException, ExecutionException {
        this.executeHotspotTask();
    }
}
