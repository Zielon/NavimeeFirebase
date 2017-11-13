package com.navimee.places.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.FacebookPlace;
import com.navimee.contracts.models.places.FoursquarePlace;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.places.queries.FacebookPlacesQuery;
import com.navimee.places.queries.FoursquarePlacesQuery;
import com.navimee.places.queries.PlacesParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.navimee.asynchronous.HelperMethods.waitForAll;
import static com.navimee.firestoreHelpers.Distinct.distinctByKey;

@Service
public class PlacesServiceImpl implements PlacesService {

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Autowired
    FoursquareConfiguration foursquareConfiguration;

    @Override
    public List<FacebookPlace> getFacebookPlaces(List<Coordinate> coordinates) {
        FacebookPlacesQuery facebookPlacesQuery = new FacebookPlacesQuery(facebookConfiguration);
        List<Future<List<FacebookPlace>>> futures = coordinates.stream().map(
                c -> {
                    PlacesParams params = new PlacesParams(c.latitude, c.longitude);
                    return facebookPlacesQuery.execute(params);
                }
        ).collect(Collectors.toList());

        return waitForAll(futures).stream().filter(distinctByKey(p -> p.id)).collect(Collectors.toList());
    }

    @Override
    public List<FoursquarePlace> getFoursquarePlaces(List<Coordinate> coordinates) {

        FoursquarePlacesQuery foursquarePlacesQuery = new FoursquarePlacesQuery(foursquareConfiguration);
        List<Future<List<FoursquarePlace>>> futures = coordinates.stream().map(
                c -> {
                    PlacesParams params = new PlacesParams(c.latitude, c.longitude);
                    return foursquarePlacesQuery.execute(params);
                }
        ).collect(Collectors.toList());

        return waitForAll(futures).stream().filter(distinctByKey(p -> p.id)).collect(Collectors.toList());
    }
}
