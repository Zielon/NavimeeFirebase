package com.navimee.tasks.places;

import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.mappers.boToEntity.FsPlaceBoMapper;
import com.navimee.mappers.boToEntity.PlaceBoMapper;
import com.navimee.models.bussinesObjects.places.FsPlaceBo;
import com.navimee.models.bussinesObjects.places.PlaceBo;
import com.navimee.models.entities.general.City;
import com.navimee.models.entities.general.Coordinate;
import com.navimee.models.entities.places.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class PlacesTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    // Once per 30 days.
    //@Scheduled(cron = "0 00 12 ? * *")
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void addPlacesTask() throws ExecutionException, InterruptedException {

        // Mocked data.
        com.navimee.mockups.NavimeeData navimeeData = new com.navimee.mockups.NavimeeData();
        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> cities = navimeeData.getCities();

        //placesRepository.deleteCollection("availableCities").get();
        //placesRepository.deleteCollection("events").get();
        //placesRepository.deleteCollection("coordinates").get();

        //placesRepository.setCoordinates(coordinates).get();
        //placesRepository.setAvailableCities(cities).get();

        placesRepository.deleteCollection("places").get();
        placesRepository.deleteCollection("foursquarePlaces").get();

        placesRepository.getAvailableCities().forEach(city -> {
                    String name = city.name;
                    if (name.equals("SOPOT"))
                        Executors.newSingleThreadExecutor().submit(() -> {
                            try {
                                List<PlaceBo> facebookPlaces = placesService.downloadFacebookPlaces(name);
                                List<FsPlaceBo> foursquarePlaces = placesService.downloadFoursquarePlaces(name);

                                List<Place> places = new ArrayList<>();
                                places.addAll(facebookPlaces.stream().map(PlaceBoMapper.INSTANCE::toPlace).collect(Collectors.toList()));
                                places.addAll(foursquarePlaces.stream().filter(e -> e.getFacebookId() != null).map(FsPlaceBoMapper.INSTANCE::toPlace).collect(Collectors.toList()));

                                placesRepository.setFoursquarePlaces(foursquarePlaces.stream().map(FsPlaceBoMapper.INSTANCE::toPlace).collect(Collectors.toList()), name);
                                placesRepository.setPlaces(places, name);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        });
                }
        );
    }
}
