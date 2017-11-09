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

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class NavimeeApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context = SpringApplication.run(NavimeeApplication.class, args);
        PlacesRepository placesRepository = context.getBean(PlacesRepository.class);
        PlacesService placesService = context.getBean(PlacesService.class);

        NavimeeData navimeeData = new NavimeeData();

        Map<String, List<Coordinate>> coordinates = navimeeData.getCoordinates();
        List<City> cities = navimeeData.getCities();

        // Wait
        placesRepository.setCoordinates(coordinates).get();
        placesRepository.setAvailableCities(cities).get();

        List<Place> places = placesService.getFacebookPlaces(placesRepository.getCoordinates("WARSAW"));
        placesRepository.setPlaces(places, "WARSAW").get();

        List<FacebookPlace> fb = placesRepository.getPlaces("WARSAW", FacebookPlace.class);
    }
}
