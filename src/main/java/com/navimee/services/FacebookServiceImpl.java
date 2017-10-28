package com.navimee.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.configuration.FacebookConfiguration;
import com.navimee.contracts.repositories.NavimeeRepository;
import com.navimee.contracts.services.FacebookService;
import com.navimee.contracts.services.HttpClient;
import com.navimee.entities.Coordinate;
import com.navimee.models.Event;
import com.navimee.models.Place;
import com.navimee.queries.EventsQuery;
import com.navimee.queries.PlacesQuery;
import com.navimee.queries.QueryFactory;
import com.sun.xml.internal.ws.util.CompletedFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class FacebookServiceImpl implements FacebookService {

    @Autowired
    NavimeeRepository navimeeRepository;

    @Autowired
    FacebookConfiguration facebookConfiguration;

    private QueryFactory factory = new QueryFactory();

    @Override
    public List<Event> getEvents() {

        EventsQuery query = factory.getQuery(EventsQuery.class);
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

        return events;
    }

    @Override
    public List<Place> getPlaces() {

        try {
            PlacesQuery query = factory.getQuery(PlacesQuery.class);
            List<Place> tasks = new ArrayList<>();
            navimeeRepository.getCoordinates().forEach((Coordinate c) ->{
                query.setCoordinates(c.getLatitude(), c.getLongitude());
                try {
                    tasks.addAll(query.get(facebookConfiguration).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
            return tasks;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
