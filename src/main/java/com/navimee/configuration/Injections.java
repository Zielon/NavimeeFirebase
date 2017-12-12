package com.navimee.configuration;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.configuration.mappers.FsPlacesDetailsConverter;
import com.navimee.configuration.specific.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class Injections {

    @Value(value = "classpath:firestore-services.json")
    private Resource firebaseConfig;

    @Value(value = "classpath:facebook-services.json")
    private Resource facebookConfig;

    @Value(value = "classpath:foursquare-services.json")
    private Resource foursquareConfig;

    @Value(value = "classpath:google-geo-services.json")
    private Resource googleGeoConfig;

    @Value(value = "classpath:google-fmc-services.json")
    private Resource googleFmcConfig;

    @Bean
    FirebaseConfiguration providerFirebaseConfiguration() throws IOException {
        return new FirebaseConfiguration(firebaseConfig);
    }

    @Bean
    FacebookConfiguration providerFacebookConfiguration() throws IOException {
        return new FacebookConfiguration(facebookConfig);
    }

    @Bean
    FoursquareConfiguration providerFoursquareConfiguration() throws IOException {
        return new FoursquareConfiguration(foursquareConfig);
    }

    @Bean
    GoogleConfiguration providerGoogleConfiguration() throws IOException {
        return new GoogleConfiguration(googleGeoConfig, googleFmcConfig);
    }

    @Bean
    public ModelMapper modelMapperProvider() {
        ModelMapper modalMapper = new ModelMapper();

        modalMapper.addConverter(FsPlacesDetailsConverter.getConverter());

        modalMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modalMapper;
    }

    @Bean
    @Scope("singleton")
    public Firestore providerFirestore() {
        return FirebaseInitialization.getFirestoreReference(firebaseConfig);
    }

    @Bean
    @Scope("singleton")
    public FirebaseDatabase providerFirebase() {
        return FirebaseInitialization.getFirebaseReference(firebaseConfig);
    }

    @Bean
    @Scope("singleton")
    public ExecutorService providerExecutorService() {
        return Executors.newWorkStealingPool();
    }
}
