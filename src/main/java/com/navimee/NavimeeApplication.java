package com.navimee;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.contracts.repositories.NavimeeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class NavimeeApplication extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(NavimeeApplication.class, args);
		NavimeeRepository navimeeRepository = context.getBean(NavimeeRepository.class);

		navimeeRepository.addCities();
		navimeeRepository.addCoordinates();
	}

	private static DatabaseReference databaseReference;

	public static DatabaseReference getDatabaseReference() {
		if(databaseReference == null){
			FileInputStream serviceAccount = null;
			try {
				ClassLoader classLoader = NavimeeApplication.class.getClassLoader();
				// Getting resource(File) from class loader
				File configFile = new File(classLoader.getResource("google-services.json").getFile());
				serviceAccount = new FileInputStream(configFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			FirebaseOptions options = null;
			try {
				options = new FirebaseOptions.Builder()
                        .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                        .setDatabaseUrl("https://navimee-1a213.firebaseio.com")
                        .build();
			} catch (IOException e) {
				e.printStackTrace();
			}

			FirebaseApp.initializeApp(options);
			databaseReference = FirebaseDatabase.getInstance().getReference();
		}

		return databaseReference;
	}
}
