package com.navimee;

import com.mashape.unirest.http.Unirest;
import com.navimee.contracts.repositories.NavimeeRepository;
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

        Unirest.setConcurrency(2000, 200);

        ConfigurableApplicationContext context = SpringApplication.run(NavimeeApplication.class, args);
        NavimeeRepository navimeeRepository = context.getBean(NavimeeRepository.class);

        navimeeRepository.addCoordinates();
        navimeeRepository.addAvailableCities();

        //navimeeRepository.getAvailableCities();
        navimeeRepository.getCoordinates();
    }
}
