package com.navimee.places.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.models.placeDetails.FoursquarePlaceDetails;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.FacebookPlace;
import com.navimee.contracts.models.places.FoursquarePlace;
import com.navimee.contracts.models.places.GooglePlace;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.places.queries.FacebookPlacesQuery;
import com.navimee.places.queries.FoursquareDetailsQuery;
import com.navimee.places.queries.FoursquarePlacesQuery;
import com.navimee.places.queries.GoogleGeocodingQuery;
import com.navimee.places.queries.params.PlaceDetailsParams;
import com.navimee.places.queries.params.PlacesParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.navimee.asyncCollectors.HelperMethods.*;
import static com.navimee.linq.Distinct.distinctByKey;

@Service
public class PlacesServiceImpl implements PlacesService {

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Autowired
    FoursquareConfiguration foursquareConfiguration;

    @Autowired
    GoogleConfiguration googleConfiguration;

    @Override
    public List<FacebookPlace> getFacebookPlaces(List<Coordinate> coordinates) {
        FacebookPlacesQuery facebookPlacesQuery = new FacebookPlacesQuery(facebookConfiguration);
        List<Future<List<FacebookPlace>>> futures = coordinates.stream().map(
                c -> facebookPlacesQuery.execute(new PlacesParams(c.latitude, c.longitude))
        ).collect(Collectors.toList());

        return waitForMany(futures).stream().filter(distinctByKey(p -> p.id)).collect(Collectors.toList());
    }

    @Override
    public List<FoursquarePlace> getFoursquarePlaces(List<Coordinate> coordinates) {
        FoursquarePlacesQuery foursquarePlacesQuery = new FoursquarePlacesQuery(foursquareConfiguration);
        List<Future<List<FoursquarePlace>>> futures = coordinates.stream().map(
                c -> foursquarePlacesQuery.execute(new PlaceDetailsParams(c.latitude, c.longitude, "/venues/search"))
        ).collect(Collectors.toList());

        return waitForMany(futures).stream().filter(distinctByKey(p -> p.id)).collect(Collectors.toList());
    }

    @Override
    public List<FoursquarePlaceDetails> getFoursquarePlacesDetails(List<FoursquarePlace> places) {
        FoursquareDetailsQuery query = new FoursquareDetailsQuery(foursquareConfiguration);
        List<Future<FoursquarePlaceDetails>> futures = new ArrayList<>();
        places.forEach(p -> futures.add(query.execute(new PlaceDetailsParams("venues", p.id))));

        return waitForAll(futures).stream()
                .filter(distinctByKey(d -> d.id))
                .filter(d -> d.stats.checkinsCount > 500)
                .collect(Collectors.toList());
    }

    @Override
    public GooglePlace getReverseGeocoding(Coordinate coordinate) {
        GoogleGeocodingQuery query = new GoogleGeocodingQuery(googleConfiguration);
        return waitForSingle(query.execute(new PlacesParams(coordinate.latitude, coordinate.longitude)));
    }
}
