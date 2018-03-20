package com.navimee.services.places;

import com.navimee.configuration.Qualifiers;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import com.navimee.contracts.repositories.places.PlacesDetailsRepository;
import com.navimee.contracts.services.HttpClient;
import com.navimee.contracts.services.places.PlacesDetailsService;
import com.navimee.foursquareCategories.CategoryTree;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.dto.categories.FsCategoriesDto;
import com.navimee.models.dto.placeDetails.FsPlaceDetailsDto;
import com.navimee.models.dto.places.foursquare.FsPlaceDto;
import com.navimee.models.dto.timeframes.PopularDto;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.places.Place;
import com.navimee.models.entities.places.foursquare.FsPlace;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import com.navimee.models.entities.places.foursquare.popularHours.FsPopular;
import com.navimee.queries.categories.FoursquareCategoryQuery;
import com.navimee.queries.places.FoursquareDetailsQuery;
import com.navimee.queries.places.FoursquarePlacesQuery;
import com.navimee.queries.places.FoursquareTimeFramesQuery;
import com.navimee.queries.places.params.PlaceDetailsParams;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
@Qualifier(Qualifiers.FOURSQUARE)
public class FoursquarePlacesServiceImpl implements PlacesDetailsService {

    private static int COUNTER = 0;
    @Autowired
    FoursquareConfiguration foursquareConfiguration;
    @Autowired
    CoordinatesRepository coordinatesRepository;
    @Autowired
    @Qualifier(Qualifiers.FOURSQUARE)
    PlacesDetailsRepository<FsPlaceDetails, FsPlace> foursquareRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ExecutorService executorService;
    @Autowired
    HttpClient httpClient;
    @Autowired
    FirebaseRepository firebaseRepository;

    @Override
    public CompletableFuture<Void> savePlacesDetails(String city) {
        return foursquareRepository.getPlaces(city).thenAcceptAsync(places -> {
            FoursquareDetailsQuery placesQuery =
                    new FoursquareDetailsQuery(foursquareConfiguration, executorService, httpClient);

            List<CompletableFuture<FsPlaceDetailsDto>> placesTasks = new ArrayList<>();
            try {
                for (FsPlace place : places) {
                    placesTasks.add(placesQuery.execute(new PlaceDetailsParams("venues", place.getId())));
                    COUNTER++;
                    if (COUNTER >= 4000) {
                        //TimeUnit.HOURS.sleep(1);
                        COUNTER = 0;
                        break;
                    }
                }
                // Wait even longer
                //TimeUnit.HOURS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            sequence(placesTasks).thenAcceptAsync(tasks ->

                    getCategoryTree().thenAcceptAsync(categoryTree -> {

                        List<FsPlaceDetails> entitiesDetails = tasks.stream()
                                .filter(Objects::nonNull)
                                .map(dto -> modelMapper.map(dto, FsPlaceDetails.class))
                                .filter(distinctByKey(FsPlaceDetails::getId))
                                .filter(d -> d.getStatsCheckinsCount() > 600 || d.getLikesCount() > 600)
                                .filter(categoryTree.getPredicate())
                                .collect(toList());

                        // Update timeframes for every place
                        FoursquareTimeFramesQuery timeframeQuery =
                                new FoursquareTimeFramesQuery(foursquareConfiguration, executorService, httpClient);

                        List<CompletableFuture<PopularDto>> popularTasks = entitiesDetails.stream()
                                .map(p -> timeframeQuery.execute(new PlaceDetailsParams("venues", p.getId())))
                                .collect(toList());

                        sequence(popularTasks).thenAcceptAsync(populars -> {
                            populars.stream().filter(Objects::nonNull)
                                    .forEach(dto -> entitiesDetails.stream()
                                            .filter(details -> details.getId().equals(dto.getPlaceId()))
                                            .findFirst().get()
                                            .setPopular(modelMapper.map(dto, FsPopular.class)));

                            List<FsPlaceDetails> entities = entitiesDetails.stream()
                                    .filter(details -> details.getPopular() != null).collect(toList());

                            foursquareRepository.setPlacesDetails(entities);
                            firebaseRepository.transferPlaces(entities);

                        }, executorService).thenRunAsync(() -> Logger.LOG(new Log(LogTypes.TASK, "Foursquare details update for %s [FS]", city)));
                    })
            );
        });
    }

    @Override
    public CompletableFuture<CategoryTree> getCategoryTree() {

        FoursquareCategoryQuery foursquareCategoryQuery =
                new FoursquareCategoryQuery(foursquareConfiguration, executorService, httpClient);

        return foursquareCategoryQuery.execute(null).thenApplyAsync(categoriesDtos -> {
            List<FsCategoriesDto> categories = categoriesDtos.stream().filter(Objects::nonNull).collect(toList());
            return new CategoryTree().build(categories);
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> savePlaces(String city) {
        return coordinatesRepository.getCoordinates(city).thenAcceptAsync(coordinates -> {
            // DbGet data from the external foursquare API
            FoursquarePlacesQuery foursquarePlacesQuery =
                    new FoursquarePlacesQuery(foursquareConfiguration, executorService, httpClient);

            List<CompletableFuture<List<FsPlaceDto>>> tasks = coordinates.stream()
                    .map(c -> foursquarePlacesQuery.execute(new PlaceDetailsParams(c.getLatitude(), c.getLongitude(), "/venues/search")))
                    .collect(toList());

            sequence(tasks).thenAcceptAsync(places -> {
                List<FsPlace> entities = places.parallelStream()
                        .flatMap(Collection::stream)
                        .filter(Objects::nonNull)
                        .map(dto -> modelMapper.map(dto, FsPlace.class))
                        .filter(distinctByKey(Place::getId))
                        .collect(toList());

                foursquareRepository.setPlaces(entities, city).join();

            }, executorService).exceptionally(throwable -> {
                Logger.LOG(new Log(LogTypes.EXCEPTION, throwable));
                return null;
            }).thenRunAsync(() -> Logger.LOG(new Log(LogTypes.TASK, "Foursquare places update for %s [FS]", city)));
        });
    }
}
