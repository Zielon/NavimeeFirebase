package com.navimee;

import com.navimee.contracts.repositories.palces.PlacesRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class NavimeeApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context = SpringApplication.run(NavimeeApplication.class, args);
        PlacesRepository placesRepository = context.getBean(PlacesRepository.class);

        //placesRepository.getCoordinates("WARSAW");
        placesRepository.setCoordinates();
    }
}
