package com.navimee.events.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.events.EventsService;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.events.Events;
import com.navimee.events.queries.FacebookEventsQuery;
import com.navimee.events.queries.params.EventsParams;
import com.navimee.mappers.dtoToBo.EventDtoMapper;
import com.navimee.mappers.enityToBo.EventEntityMapper;
import com.navimee.models.bussinesObjects.events.FbEventBo;
import com.navimee.models.bussinesObjects.general.CoordinateBo;
import com.navimee.models.entities.places.Place;
import com.navimee.models.externalDto.events.FbEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static com.navimee.asyncCollectors.HelperMethods.waitForMany;
import static com.navimee.events.Events.complement;
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

    @Override
    public List<FbEventBo> downloadFacebookEvents(String city) {
        List<Place> places = placesRepository.getPlaces(city);
        List<Future<List<FbEventDto>>> events = new ArrayList<>();
        places.forEach(place -> events.add(new FacebookEventsQuery(facebookConfiguration).execute(new EventsParams(place))));

        return waitForMany(events)
                .parallelStream()
                .map(EventDtoMapper.INSTANCE::toEventBo)
                .filter(distinctByKey(FbEventBo::getId))
                .parallel()
                .filter(event ->
                        complement(event,
                                event.getPlace() != null ? placesService.downloadReverseGeocoding(new CoordinateBo(event.getPlace().getLat(), event.getPlace().getLon())) : null,
                                placesService.downloadReverseGeocoding(new CoordinateBo(event.getSearchPlace().getLat(), event.getSearchPlace().getLon()))))
                .collect(toList());
    }

    @Override
    public Map<String, List<FbEventBo>> sevenDaysSegregation(String city) {
        List<FbEventBo> events = eventsRepository.getEvents(city)
                .stream()
                .map(EventEntityMapper.INSTANCE::toEvent)
                .collect(toList());

        return Events.sevenDaysSegregation(events);
    }
}
