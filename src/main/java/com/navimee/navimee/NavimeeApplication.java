package com.navimee.navimee;

import com.navimee.navimee.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
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

         /*
            TODO: Initialize firebase service
         */
		context.getBean(FirebaseService.class).startFirebaseListener();

	}
}
