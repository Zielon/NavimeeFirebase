package com.navimee.tasks;

import com.firebase.geofire.GeoLocation;
import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.contracts.repositories.places.PlacesDetailsRepository;
import com.navimee.linq.HotspotFilters;
import com.navimee.models.entities.places.foursquare.FsPlace;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.navimee.tasks.TasksFixedTimes.HOTSPOT;

@Component
public class HotspotTask {

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    FirebaseRepository firebaseRepository;

    @Autowired
    PlacesDetailsRepository<FsPlaceDetails, FsPlace> foursquareRepository;

    public void executeHotspotTask() {

        firebaseRepository.deleteCurrentHotspot();

        foursquareRepository.getPlacesDetails().thenAcceptAsync(placeDetails -> {
            firebaseRepository.filterAndTransfer(
                    placeDetails,
                    HotspotFilters.filterFsPopular(),
                    fsPlaceDetails -> new GeoLocation(fsPlaceDetails.getLocationLat(), fsPlaceDetails.getLocationLng()));
        });

        eventsRepository.getEventsBefore(60 * 2).thenAcceptAsync(events -> {
            firebaseRepository.filterAndTransfer(
                    events,
                    event -> true,
                    event -> new GeoLocation(event.getGeoPoint().getLatitude(), event.getGeoPoint().getLongitude()));
        });
    }

    @Scheduled(fixedDelay = HOTSPOT)
    public void task() {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executeHotspotTask();
    }
}
