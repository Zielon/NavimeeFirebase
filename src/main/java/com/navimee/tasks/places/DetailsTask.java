package com.navimee.tasks.places;

import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.mappers.enityToBo.FsPlaceDetailsEntityMapper;
import com.navimee.models.bussinesObjects.places.FsPlaceDetailsBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

@Component
public class DetailsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    //@Scheduled(fixedRate = 1000 * 60 * 60)
    public void addDetailsTask() throws ExecutionException, InterruptedException {

        placesRepository.getAvailableCities().forEach(city -> {
                    if (city.name.equals("GDANSK"))
                        Executors.newSingleThreadExecutor().submit(() -> {
                                    List<FsPlaceDetailsBo> details = placesService.downloadFoursquarePlacesDetails(city.name);
                                    placesRepository.setFoursquarePlacesDetails(
                                            details.stream()
                                                    .map(FsPlaceDetailsEntityMapper.INSTANCE::toFsPlaceDetails)
                                                    .collect(toList()),
                                            city.name);
                                }
                        );
                }
        );
    }
}
