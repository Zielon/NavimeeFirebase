package com.navimee.events.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.models.events.Event;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.services.events.EventsService;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.events.queries.FacebookEventsQuery;
import com.navimee.events.queries.params.EventsParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.navimee.asyncCollectors.HelperMethods.waitForMany;
import static com.navimee.events.Events.complement;
import static com.navimee.linq.Distinct.distinctByKey;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Autowired
    PlacesService placesService;

    @Override
    public List<Event> getFacebookEvents(List<Place> places) {

        List<Future<List<Event>>> events = new ArrayList<>();
        places.forEach(place -> events.add(new FacebookEventsQuery(facebookConfiguration).execute(new EventsParams(place))));

        return waitForMany(events)
                .stream()
                .filter(distinctByKey(e -> e.id))
                .parallel()
                .filter(event ->
                        complement(event,
                                placesService.getReverseGeocoding(new Coordinate(event.place.lat, event.place.lon)),
                                placesService.getReverseGeocoding(new Coordinate(event.searchPlace.lat, event.searchPlace.lon))))
                .collect(Collectors.toList());
    }
}
