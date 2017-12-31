package com.navimee.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.contracts.services.HttpClient;
import com.navimee.contracts.services.PlacesService;
import com.navimee.general.Collections;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.models.dto.placeDetails.FsPlaceDetailsDto;
import com.navimee.models.dto.places.facebook.FbPlaceDto;
import com.navimee.models.dto.places.foursquare.FsPlaceDto;
import com.navimee.models.dto.timeframes.PopularDto;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.Coordinate;
import com.navimee.models.entities.places.Place;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.models.entities.places.foursquare.FsPlace;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import com.navimee.models.entities.places.foursquare.popularHours.FsPopular;
import com.navimee.queries.places.*;
import com.navimee.queries.places.params.PlaceDetailsParams;
import com.navimee.queries.places.params.PlacesParams;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.navimee.asyncCollectors.CompletionCollector.waitForManyTasks;
import static com.navimee.asyncCollectors.CompletionCollector.waitForSingleTask;
import static com.navimee.linq.Distinct.distinctByKey;
import static java.util.stream.Collectors.toList;

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

    @Autowired
    HttpClient httpClient;

    @Autowired
    FirebaseRepository firebaseRepository;

    @Override
    public Future saveFacebookPlaces(String city) {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogTypes.TASK, "Facebook places update"));
            List<Coordinate> coordinates = placesRepository.getCoordinates(city);

            // DbGet data from the external facebook API
            FacebookPlacesQuery facebookPlacesQuery =
                    new FacebookPlacesQuery(facebookConfiguration, executorService, httpClient);

            List<Callable<List<FbPlaceDto>>> tasks = coordinates.stream()
                    .map(c -> facebookPlacesQuery.execute(new PlacesParams(c.getLatitude(), c.getLongitude())))
                    .collect(toList());

            List<FbPlace> entities = waitForManyTasks(executorService, tasks)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(dto -> modelMapper.map(dto, FbPlace.class))
                    .filter(distinctByKey(Place::getId))
                    .collect(toList());

            placesRepository.setFacebookPlaces(entities, city);
        });
    }

    @Override
    public Future saveFoursquarePlaces(String city) {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogTypes.TASK, "Foursquare places update"));
            List<Coordinate> coordinates = placesRepository.getCoordinates(city);

            // DbGet data from the external foursquare API
            FoursquarePlacesQuery foursquarePlacesQuery =
                    new FoursquarePlacesQuery(foursquareConfiguration, executorService, httpClient);

            List<Callable<List<FsPlaceDto>>> tasks = coordinates.stream()
                    .map(c -> foursquarePlacesQuery.execute(new PlaceDetailsParams(c.getLatitude(), c.getLongitude(), "/venues/search")))
                    .collect(toList());

            List<FsPlace> entities = waitForManyTasks(executorService, tasks)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(dto -> modelMapper.map(dto, FsPlace.class))
                    .filter(distinctByKey(Place::getId))
                    .collect(toList());

            placesRepository.setFoursquarePlaces(entities, city);
        });
    }

    @Override
    public Future saveFoursquarePlacesDetails(String city) {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogTypes.TASK, "Foursquare details update for " + city));
            List<FsPlace> places = placesRepository.getFoursquarePlaces(city);

            // DbGet data from the external foursquare API
            FoursquareDetailsQuery placesQuery =
                    new FoursquareDetailsQuery(foursquareConfiguration, executorService, httpClient);

            List<Callable<FsPlaceDetailsDto>> placesTasks = new ArrayList<>();
            List<FsPlaceDetailsDto> placesDto = new ArrayList<>();

            Collections.spliter(places, 4000).forEach(subPlaces -> {
                try {
                    subPlaces.forEach(p -> placesTasks.add(placesQuery.execute(new PlaceDetailsParams("venues", p.getId()))));
                    placesDto.addAll(waitForSingleTask(executorService, placesTasks));
                    TimeUnit.HOURS.sleep(1);
                    placesTasks.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            List<FsPlaceDetails> entitiesDetails = placesDto.parallelStream()
                    .filter(Objects::nonNull)
                    .map(dto -> modelMapper.map(dto, FsPlaceDetails.class))
                    .filter(distinctByKey(FsPlaceDetails::getId))
                    .filter(d -> d.getStatsCheckinsCount() > 300)
                    .collect(toList());

            // Update timeframes for every place
            FoursquareTimeFramesQuery timeframeQuery =
                    new FoursquareTimeFramesQuery(foursquareConfiguration, executorService, httpClient);

            List<Callable<PopularDto>> popularTasks = new ArrayList<>();
            entitiesDetails.forEach(p -> popularTasks.add(timeframeQuery.execute(new PlaceDetailsParams("venues", p.getId()))));

            waitForSingleTask(executorService, popularTasks).stream()
                    .filter(Objects::nonNull)
                    .forEach(dto -> entitiesDetails.stream()
                            .filter(details -> details.getId().equals(dto.getPlaceId()))
                            .findFirst().get()
                            .setPopular(modelMapper.map(dto, FsPopular.class)));

            List<FsPlaceDetails> entities = entitiesDetails.stream()
                    .filter(details -> details.getPopular() != null).collect(toList());

            placesRepository.setFoursquarePlacesDetails(entities);
            firebaseRepository.transferPlaces(entities);
        });
    }

    @Override
    public Future<GooglePlaceDto> downloadReverseGeocoding(Coordinate coordinate) {
        GoogleGeocodingQuery query = new GoogleGeocodingQuery(googleConfiguration, executorService, httpClient);
        return executorService.submit(() -> query.execute(new PlacesParams(coordinate.getLatitude(), coordinate.getLongitude())).call());
    }
}
