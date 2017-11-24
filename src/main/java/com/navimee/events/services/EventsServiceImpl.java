package com.navimee.events.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.events.EventsService;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.events.Events;
import com.navimee.events.queries.FacebookEventsQuery;
import com.navimee.events.queries.params.EventsParams;
import com.navimee.models.entities.events.FbEvent;
import com.navimee.models.entities.general.Coordinate;
import com.navimee.models.entities.places.Place;
import com.navimee.models.externalDto.events.FbEventDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Function;

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

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void saveFacebookEvents(String city) {

        // Get data from the external facebook API
        List<Place> places = placesRepository.getPlaces(city);
        List<Future<List<FbEventDto>>> events = new ArrayList<>();
        places.forEach(place -> events.add(new FacebookEventsQuery(facebookConfiguration).execute(new EventsParams(place))));

        // TODO improve the geocoding code.
        Function<FbEvent, Boolean> func = event -> {
            Coordinate place = new Coordinate(event.getPlace().getLat(), event.getPlace().getLon());
            Coordinate searchPlace = new Coordinate(event.getSearchPlace().getLat(), event.getSearchPlace().getLon());

            return complement(event,
                    event.getPlace() != null ? placesService.downloadReverseGeocoding(place) : null,
                    placesService.downloadReverseGeocoding(searchPlace));
        };

        List<FbEvent> entities = waitForMany(events)
                .parallelStream()
                .map(dto -> modelMapper.map(dto, FbEvent.class))
                .filter(distinctByKey(FbEvent::getId))
                .filter(func::apply)
                .collect(toList());

        // Save data
        eventsRepository.setEvents(entities, city);

    }

    @Override
    public void saveSevenDaysSegregation(String city) {
        Map<String, List<FbEvent>> segregation = Events.sevenDaysSegregation(eventsRepository.getEvents(city));
        eventsRepository.sevenDaysSegregation(segregation, city);
    }
}
