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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.navimee.tasks.TasksFixedTimes.HOTSPOT;

@Component
public class HotspotTask {

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    FirebaseRepository firebaseRepository;

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    ExecutorService executorService;

    public void executeHotspotTask() {
        Logger.LOG(new Log(LogEnum.TASK, "Hotspot update"));
        List<Future> futures = new ArrayList<>();

        firebaseRepository.deleteCurrentHotspot();

        futures.add(firebaseRepository.filterAndTransfer(
                placesRepository.getFoursquarePlacesDetails(),
                HotspotFilters.filterFsPopular(),
                fsPlaceDetails -> new GeoLocation(fsPlaceDetails.getLocationLat(), fsPlaceDetails.getLocationLng())));

        futures.add(firebaseRepository.filterAndTransfer(
                eventsRepository.getEventsBefore(60 * 2),
                event -> true,
                event -> new GeoLocation(event.getPlace().getGeoPoint().getLatitude(), event.getPlace().getGeoPoint().getLongitude())));
    }

    @Scheduled(fixedDelay = HOTSPOT)
    public void task() {
        this.executeHotspotTask();
    }
}
