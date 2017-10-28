package com.navimee.services;

import com.navimee.configuration.FacebookConfiguration;
import com.navimee.contracts.repositories.NavimeeRepository;
import com.navimee.contracts.services.FacebookService;
import com.navimee.entities.Coordinate;
import com.navimee.models.Event;
import com.navimee.models.Place;
import com.navimee.queries.EventsQuery;
import com.navimee.queries.PlacesQuery;
import com.navimee.queries.QueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class FacebookServiceImpl implements FacebookService {

    @Autowired
    NavimeeRepository navimeeRepository;

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Override
    public List<Event> getEvents() {

        EventsQuery query = QueryFactory.getQuery(EventsQuery.class);
        List<Event> events = new ArrayList<>();

        getPlaces().forEach(p -> {
            try {
                query.setId(p.id);
                events.addAll(query.get(facebookConfiguration).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        return events.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Place> getPlaces() {

        PlacesQuery query = QueryFactory.getQuery(PlacesQuery.class);
        List<Place> tasks = new ArrayList<>();

        navimeeRepository.getCoordinates().forEach((Coordinate c) ->{
            try {
                query.setCoordinates(c.getLatitude(), c.getLongitude());
                tasks.addAll(query.get(facebookConfiguration).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        return tasks.stream().distinct().collect(Collectors.toList());
    }
}
