package com.navimee;

import com.navimee.contracts.repositories.NavimeeRepository;
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
		NavimeeRepository navimeeRepository = context.getBean(NavimeeRepository.class);

		//navimeeRepository.addCities();
		//navimeeRepository.addCoordinates();
	}
}
