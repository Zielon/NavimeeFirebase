package com.navimee.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.contracts.services.EventsService;
import com.navimee.contracts.services.HttpClient;
import com.navimee.contracts.services.places.GeoService;
import com.navimee.general.Collections;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.bo.FbEvent;
import com.navimee.models.dto.events.FbEventDto;
import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.models.entities.Event;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.queries.events.EventsHelpers;
import com.navimee.queries.events.FacebookEventsQuery;
import com.navimee.queries.events.params.FacebookEventsParams;
import com.navimee.staticData.NavimeeData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.navimee.asyncCollectors.Completable.sequence;
import static com.navimee.linq.Distinct.distinctByKey;
import static java.util.stream.Collectors.toList;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    GeoService<GooglePlaceDto> geoService;

    @Autowired
    ExecutorService executorService;

    @Autowired
    HttpClient httpClient;

    @Autowired
    FirebaseRepository firebaseRepository;

    @Override
    public CompletableFuture<Void> saveFacebookEvents(List<FbPlace> fbPlaces, boolean complement) {

        List<String> forbiddenPlaces = new NavimeeData().getPlacesBlackList();

        fbPlaces = fbPlaces.stream().filter(fbPlace -> !forbiddenPlaces.contains(fbPlace.getId())).collect(toList());

        // DbGet data from the external facebook API
        List<CompletableFuture<List<FbEventDto>>> tasks = new ArrayList<>();
        FacebookEventsQuery query = new FacebookEventsQuery(facebookConfiguration, executorService, httpClient);
        Collections.spliter(fbPlaces, 50)
                .forEach(places -> tasks.add(query.execute(new FacebookEventsParams(places))));

        return sequence(tasks).thenAcceptAsync(events -> {
            List<Event> entities = events.parallelStream()
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
                    .filter(distinctByKey(FbEventDto::getId))
                    .filter(dto -> !complement | EventsHelpers.getCompelmentFunction(geoService).apply(dto))    // Check the right place
                    .map(dto -> modelMapper.map(dto, FbEvent.class))
                    .filter(bo -> bo.getPlace() != null)
                    .map(bo -> modelMapper.map(bo, Event.class))
                    .collect(toList());

            eventsRepository.setEvents(entities);
            firebaseRepository.transferEvents(entities);

        }, executorService).exceptionally(throwable -> {
            Logger.LOG(new Log(LogTypes.EXCEPTION, throwable));
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> savePredictHqEvents(String city) {
        throw new NotImplementedException();
    }
}
