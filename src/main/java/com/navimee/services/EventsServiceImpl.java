package com.navimee.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.contracts.services.HttpClient;
import com.navimee.contracts.services.PlacesService;
import com.navimee.events.EventsHelpers;
import com.navimee.events.queries.FacebookEventsQuery;
import com.navimee.events.queries.params.EventsParams;
import com.navimee.general.Collections;
import com.navimee.models.dto.events.FbEventDto;
import com.navimee.models.entities.events.FbEvent;
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

import static com.navimee.asyncCollectors.CompletionCollector.waitForTasks;
import static com.navimee.linq.Distinct.distinctByKey;
import static java.util.stream.Collectors.toList;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    FacebookConfiguration facebookConfiguration;

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
            List<FbPlace> fbPlaces = placesRepository.getFacebookPlaces(city);

            // DbGet data from the external facebook API
            List<Callable<List<FbEventDto>>> events = new ArrayList<>();
            FacebookEventsQuery query = new FacebookEventsQuery(facebookConfiguration, executorService, httpClient);
            Collections.spliter(fbPlaces, 50).forEach(places -> events.add(query.execute(new EventsParams(places))));

            List<FbEvent> entities = waitForTasks(executorService, events)
                    .parallelStream()
                    .filter(Objects::nonNull)
                    .map(dto -> modelMapper.map(dto, FbEvent.class))
                    .filter(distinctByKey(FbEvent::getId))
                    .filter(EventsHelpers.getCompelmentFunction(placesService)::apply)    // Complement event places
                    .collect(toList());

            eventsRepository.setEvents(entities);
            firebaseRepository.transferEvents(entities);
        });
    }
}
