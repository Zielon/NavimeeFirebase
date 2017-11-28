package com.navimee.events.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.HttpClient;
import com.navimee.contracts.services.events.EventsService;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.events.Events;
import com.navimee.events.queries.FacebookEventsQuery;
import com.navimee.events.queries.params.EventsParams;
import com.navimee.models.dto.events.FbEventDto;
import com.navimee.models.entities.events.FbEvent;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.services.HttpClientImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    // Starts a new connection with the Facebook api.
    HttpClient httpClient;

    public EventsServiceImpl() {
        httpClient = new HttpClientImpl();
    }

    @Override
    public Future saveFacebookEvents(String city) {

        List<FbPlace> places = placesRepository.getFacebookPlaces(city);

        // Get data from the external facebook API
        List<Callable<List<FbEventDto>>> events = new ArrayList<>();
        FacebookEventsQuery query = new FacebookEventsQuery(facebookConfiguration, executorService, httpClient);
        places.forEach(place -> events.add(query.execute(new EventsParams(place))));

        List<FbEvent> entities = waitForTasks(executorService, events)
                .parallelStream()
                .map(dto -> modelMapper.map(dto, FbEvent.class))
                .filter(distinctByKey(FbEvent::getId))
                .filter(Events.getCompelmentFunction(placesService)::apply)    // Complement event places
                .collect(toList());

        return eventsRepository.setEvents(entities, city);
    }
}
