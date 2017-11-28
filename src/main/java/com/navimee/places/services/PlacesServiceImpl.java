package com.navimee.places.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.HttpClient;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.models.dto.placeDetails.FsPlaceDetailsDto;
import com.navimee.models.dto.places.facebook.FbPlaceDto;
import com.navimee.models.dto.places.foursquare.FsPlaceDto;
import com.navimee.models.entities.general.Coordinate;
import com.navimee.models.entities.places.Place;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.models.entities.places.foursquare.FsPlace;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import com.navimee.places.queries.FacebookPlacesQuery;
import com.navimee.places.queries.FoursquareDetailsQuery;
import com.navimee.places.queries.FoursquarePlacesQuery;
import com.navimee.places.queries.GoogleGeocodingQuery;
import com.navimee.places.queries.params.PlaceDetailsParams;
import com.navimee.places.queries.params.PlacesParams;
import com.navimee.services.HttpClientImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.navimee.asyncCollectors.CompletionCollector.waitForSingleTask;
import static com.navimee.asyncCollectors.CompletionCollector.waitForTasks;
import static com.navimee.linq.Distinct.distinctByKey;

@Service
public class PlacesServiceImpl implements PlacesService {

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Autowired
    FoursquareConfiguration foursquareConfiguration;

    @Autowired
    GoogleConfiguration googleConfiguration;

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExecutorService executorService;

    // Starts a new client for the Facebook api.
    HttpClient httpClientFacebook;

    // Starts a new client for the Foursquare api.
    HttpClient httpClientFoursquare;

    // Starts a new client for the Google api.
    HttpClient httpClientGoogle;

    public PlacesServiceImpl() {
        httpClientFacebook = new HttpClientImpl();
        httpClientFoursquare = new HttpClientImpl();
        httpClientGoogle = new HttpClientImpl();
    }

    @Override
    public Future saveFacebookPlaces(String city) {

        return executorService.submit(() -> {
            List<Coordinate> coordinates = placesRepository.getCoordinates(city);

            // Get data from the external facebook API
            FacebookPlacesQuery facebookPlacesQuery =
                    new FacebookPlacesQuery(facebookConfiguration, executorService, httpClientFacebook);

            List<Callable<List<FbPlaceDto>>> tasks =
                    coordinates.stream()
                            .map(c -> facebookPlacesQuery.execute(new PlacesParams(c.getLatitude(), c.getLongitude())))
                            .collect(Collectors.toList());

            List<Place> entities = waitForTasks(executorService, tasks)
                    .stream()
                    .map(dto -> modelMapper.map(dto, FbPlace.class))
                    .filter(distinctByKey(Place::getId))
                    .collect(Collectors.toList());

            placesRepository.setFacebookPlaces(entities, city);
        });
    }

    @Override
    public Future saveFoursquarePlaces(String city) {

        return executorService.submit(() -> {
            List<Coordinate> coordinates = placesRepository.getCoordinates(city);

            // Get data from the external foursquare API
            FoursquarePlacesQuery foursquarePlacesQuery =
                    new FoursquarePlacesQuery(foursquareConfiguration, executorService, httpClientFoursquare);

            List<Callable<List<FsPlaceDto>>> tasks =
                    coordinates.stream()
                            .map(c -> foursquarePlacesQuery.execute(new PlaceDetailsParams(c.getLatitude(), c.getLongitude(), "/venues/search")))
                            .collect(Collectors.toList());

            List<Place> entities = waitForTasks(executorService, tasks)
                    .stream()
                    .map(dto -> modelMapper.map(dto, FsPlace.class))
                    .filter(distinctByKey(Place::getId))
                    .collect(Collectors.toList());

            placesRepository.setFoursquarePlaces(entities, city);
        });
    }

    @Override
    public Future saveFoursquarePlacesDetails(String city) {

        return executorService.submit(() -> {
            List<FsPlace> places = placesRepository.getFoursquarePlaces(city);

            // Get data from the external foursquare API
            FoursquareDetailsQuery query =
                    new FoursquareDetailsQuery(foursquareConfiguration, executorService, httpClientFoursquare);

            List<Callable<FsPlaceDetailsDto>> tasks = new ArrayList<>();
            places.forEach(p -> tasks.add(query.execute(new PlaceDetailsParams("venues", p.getId()))));


            List<FsPlaceDetails> entities = waitForSingleTask(executorService, tasks)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(dto -> modelMapper.map(dto, FsPlaceDetails.class))
                    .filter(distinctByKey(FsPlaceDetails::getId))
                    .filter(d -> d.getStatsCheckinsCount() > 500)
                    .collect(Collectors.toList());

            placesRepository.setFoursquarePlacesDetails(entities, city);
        });
    }

    @Override
    public Future<GooglePlaceDto> downloadReverseGeocoding(Coordinate coordinate) {
        GoogleGeocodingQuery query = new GoogleGeocodingQuery(googleConfiguration, executorService, httpClientGoogle);
        return executorService.submit(() -> query.execute(new PlacesParams(coordinate.getLatitude(), coordinate.getLongitude())).call());
    }
}
