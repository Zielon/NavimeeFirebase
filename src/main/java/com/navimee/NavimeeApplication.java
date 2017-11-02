package com.navimee;

import com.navimee.contracts.repositories.NavimeeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class NavimeeApplication extends SpringBootServletInitializer {

	public static List<String> logs = new ArrayList<>();

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(NavimeeApplication.class, args);
		NavimeeRepository navimeeRepository = context.getBean(NavimeeRepository.class);

		navimeeRepository.addCities();
		navimeeRepository.addCoordinates();
	}
}
