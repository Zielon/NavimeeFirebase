package com.navimee;

import com.navimee.contracts.repositories.NavimeeRepository;
import com.navimee.contracts.services.FacebookService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NavimeeApplication extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(NavimeeApplication.class, args);

		FacebookService facebookService = context.getBean(FacebookService.class);

		NavimeeRepository navimeeRepository = context.getBean(NavimeeRepository.class);
		navimeeRepository.addCities();
		navimeeRepository.addCoordinates();

		facebookService.getPlaces();
		facebookService.getEvents();

		System.out.println("End");
	}
}
