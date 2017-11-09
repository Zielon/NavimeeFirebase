package com.navimee;

import com.navimee.contracts.models.firestore.City;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.FacebookPlace;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.mockups.NavimeeData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class NavimeeApplication extends SpringBootServletInitializer {

    public static String end = "OUT !!";

    public static void main(String[] args) throws Exception {

        end = "START !!";

        ConfigurableApplicationContext context = SpringApplication.run(NavimeeApplication.class, args);
        PlacesRepository placesRepository = context.getBean(PlacesRepository.class);
        PlacesService placesService = context.getBean(PlacesService.class);

        NavimeeData navimeeData = new NavimeeData();

        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> cities = navimeeData.getCities();

        // Wait
        placesRepository.setCoordinates(coordinates).get();
        placesRepository.setAvailableCities(cities).get();

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Future<List<Place>> warsaw = executor.submit(() -> placesService.getFacebookPlaces(placesRepository.getCoordinates("WARSAW")));
        Future<List<Place>> gdansk = executor.submit(() -> placesService.getFacebookPlaces(placesRepository.getCoordinates("GDANSK")));
        Future<List<Place>> sopot = executor.submit(() -> placesService.getFacebookPlaces(placesRepository.getCoordinates("SOPOT")));

        placesRepository.setPlaces(warsaw.get(), "WARSAW").get();
        placesRepository.setPlaces(gdansk.get(), "GDANSK").get();
        placesRepository.setPlaces(sopot.get(), "SOPOT").get();

        List<FacebookPlace> fbWarsaw = placesRepository.getPlaces("WARSAW", FacebookPlace.class);
        List<FacebookPlace> fbGdansk = placesRepository.getPlaces("GDANSK", FacebookPlace.class);
        List<FacebookPlace> fbSopot = placesRepository.getPlaces("SOPOT", FacebookPlace.class);

        end = "END !!";
    }
}
