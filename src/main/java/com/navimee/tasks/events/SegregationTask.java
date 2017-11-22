package com.navimee.tasks.events;

import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.events.EventsService;
import com.navimee.mappers.boToEntity.EventBoMapper;
import com.navimee.models.entities.events.FbEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

@Component
public class SegregationTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsService eventsService;

    @Autowired
    EventsRepository eventsRepository;

    // Once per 1 hour.
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void addSegregationTask() throws ExecutionException, InterruptedException {

        placesRepository.getAvailableCities().parallelStream().forEach(city ->
                Executors.newSingleThreadExecutor().submit(() -> {
                            Map<String, List<FbEvent>> sevenDaysSegregation = new HashMap<>();
                            eventsService.sevenDaysSegregation(city.name)
                                    .entrySet()
                                    .stream()
                                    .map(entry -> sevenDaysSegregation.put(entry.getKey(), entry.getValue().stream().map(EventBoMapper.INSTANCE::toEvent).collect(toList())));

                            eventsRepository.sevenDaysSegregation(sevenDaysSegregation, city.name);
                        }
                ));
    }
}
