package com.navimee.tasks.events;

import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.events.EventsService;
import com.navimee.mappers.boToEntity.EventBoMapper;
import com.navimee.models.entities.events.FbEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;


@Component
public class EventsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    @Autowired
    EventsRepository eventsRepository;

    // Once per 1 hour.
    //@Scheduled(cron = "0 0 0/1 * * ?")
    //@Scheduled(fixedRate = 1000 * 60 * 60)
    public void addEventsTask() throws ExecutionException, InterruptedException {

        placesRepository.getAvailableCities().parallelStream().forEach(city -> {
            if (city.name.equals("SOPOT"))
                Executors.newSingleThreadExecutor().submit(() -> {
                            List<FbEvent> events = eventsService.downloadFacebookEvents(city.name).stream().map(EventBoMapper.INSTANCE::toEvent).collect(toList());
                            eventsRepository.updateEvents(events, city.name);
                        }
                );
        });
    }
}
