package com.navimee.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.configuration.specific.PredictHqConfiguration;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.contracts.services.HttpClient;
import com.navimee.contracts.services.PlacesService;
import com.navimee.events.EventsHelpers;
import com.navimee.events.queries.FacebookEventsQuery;
import com.navimee.events.queries.PredictHqEventsQuery;
import com.navimee.events.queries.params.FacebookEventsParams;
import com.navimee.events.queries.params.PredictHqEventsParams;
import com.navimee.general.Collections;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.bo.FbEvent;
import com.navimee.models.bo.PhqEvent;
import com.navimee.models.dto.events.FbEventDto;
import com.navimee.models.dto.events.PhqEventDto;
import com.navimee.models.entities.HotspotEvent;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.Coordinate;
import com.navimee.models.entities.places.facebook.FbPlace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.navimee.asyncCollectors.CompletionCollector.waitForTasks;
import static com.navimee.linq.Distinct.distinctByKey;
import static java.util.stream.Collectors.toList;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Autowired
    PredictHqConfiguration predictHqConfiguration;

    @Autowired
    PlacesService placesService;

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExecutorService executorService;

    @Autowired
    HttpClient httpClient;

    @Autowired
    FirebaseRepository firebaseRepository;

    @Override
    public Future saveFacebookEvents(String city) {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogEnum.TASK, "Events update for " + city + " [FB]"));
            List<FbPlace> fbPlaces = placesRepository.getFacebookPlaces(city);

            // DbGet data from the external facebook API
            List<Callable<List<FbEventDto>>> events = new ArrayList<>();
            FacebookEventsQuery query = new FacebookEventsQuery(facebookConfiguration, executorService, httpClient);
            Collections.spliter(fbPlaces, 50).forEach(places -> events.add(query.execute(new FacebookEventsParams(places))));

            List<HotspotEvent> entities = waitForTasks(executorService, events)
                    .parallelStream()
                    .filter(Objects::nonNull)
                    .map(dto -> modelMapper.map(dto, FbEvent.class))
                    .filter(distinctByKey(FbEvent::getId))
                    .filter(EventsHelpers.getCompelmentFunction(placesService)::apply)    // Complement event places
                    .map(dto -> modelMapper.map(dto, HotspotEvent.class))
                    .collect(toList());

            eventsRepository.setEvents(entities);
            firebaseRepository.transferEvents(entities);
        });
    }

    @Override
    public Future savePredictHqEvents(String city) {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogEnum.TASK, "Events update for " + city + " [P_HQ]"));
            List<Coordinate> coordinates = placesRepository.getCoordinates(city);

            List<Callable<List<PhqEventDto>>> events = new ArrayList<>();
            PredictHqEventsQuery query = new PredictHqEventsQuery(predictHqConfiguration, executorService, httpClient);
            Collections.spliter(coordinates, 100).forEach(coods -> {
                try {
                    events.add(query.execute(new PredictHqEventsParams(coods)));
                    TimeUnit.MINUTES.sleep(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            List<HotspotEvent> entities = waitForTasks(executorService, events).parallelStream()
                    .filter(Objects::nonNull)
                    .map(dto -> modelMapper.map(dto, PhqEvent.class))
                    .filter(distinctByKey(PhqEvent::getId))
                    .map(dto -> modelMapper.map(dto, HotspotEvent.class))
                    .collect(toList());

            eventsRepository.setEvents(entities);
            firebaseRepository.transferEvents(entities);
        });
    }
}
