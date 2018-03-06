package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.configuration.Qualifiers;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import com.navimee.contracts.repositories.places.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.City;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.staticData.NavimeeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.navimee.tasks.TasksFixedTimes.EVENTS;
import static java.util.stream.Collectors.toList;

@Component
public class FacebookEventsTask {

    @Autowired
    CoordinatesRepository coordinatesRepository;

    @Autowired
    EventsService eventsService;

    @Autowired
    @Qualifier(Qualifiers.FACEBOOK)
    PlacesRepository<FbPlace> facebookRepository;

    public void executeEventsTask() {
        coordinatesRepository.getAvailableCities().thenAcceptAsync(cities -> {
            for (City city : cities) {
                if(city.getName().equals("SOPOT"))
                facebookRepository.getPlaces(city.getId())
                        .thenAcceptAsync(fbPlaces -> eventsService.saveFacebookEvents(fbPlaces, true)
                                .thenRunAsync(() -> Logger.LOG(new Log(LogTypes.TASK, "Facebook events update for %s [FB]", city.getName()))));
            }
        }).thenRunAsync(() -> {
            List<FbPlace> places = new NavimeeData().getEventsDistributors().stream().map(FbPlace::new).collect(toList());
            eventsService.saveFacebookEvents(places, false);
        });
    }

    @Scheduled(fixedDelay = EVENTS)
    public void task() {
        if (!NavimeeApplication.TASKS_ACTIVE) return;
        this.executeEventsTask();
    }
}
