package com.navimee.configuration;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.configuration.specific.*;
import com.navimee.mappers.FbEventTransformer;
import com.navimee.mappers.FsPlacesDetailsTransformer;
import com.navimee.mappers.PhqEventTransformer;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class Injections {

    @Bean
    FirebaseConfiguration providerFirebaseConfiguration() throws IOException {
        return new FirebaseConfiguration();
    }

    @Bean
    FoursquareConfiguration providerFoursquareConfiguration() {
        return new FoursquareConfiguration();
    }

    @Bean
    FacebookConfiguration providerFacebookConfiguration() {
        return new FacebookConfiguration();
    }

    @Bean
    GoogleFcmConfiguration providerGoogleFcmConfiguration() {
        return new GoogleFcmConfiguration();
    }

    @Bean
    GoogleGeoConfiguration providerGoogleGeoConfiguration() {
        return new GoogleGeoConfiguration();
    }

    @Bean
    PredictHqConfiguration providerPredictHqConfiguration() {
        return new PredictHqConfiguration();
    }

    @Bean
    FirebaseInitialization providerFirebaseInitialization() throws IOException {
        return new FirebaseInitialization();
    }

    @Bean
    public ModelMapper modelMapperProvider() {
        ModelMapper modalMapper = new ModelMapper();

        modalMapper.addConverter(FsPlacesDetailsTransformer.get());
        modalMapper.addConverter(PhqEventTransformer.get());
        modalMapper.addConverter(FbEventTransformer.get());

        modalMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modalMapper;
    }

    @Bean
    @Scope("singleton")
    public Firestore providerFirestore(FirebaseInitialization firebaseInitialization) {
        return firebaseInitialization.getFirestore();
    }

    @Bean
    @Scope("singleton")
    public FirebaseDatabase providerFirebase(FirebaseInitialization firebaseInitialization) {
        return firebaseInitialization.getFirebase();
    }

    @Bean
    @Primary
    @Scope("singleton")
    public ExecutorService providerExecutorService() {
        return Executors.newWorkStealingPool();
    }

    @Bean
    @Scope("singleton")
    @Qualifier("scheduledExecutor")
    public ScheduledExecutorService providerScheduledExecutorService() {
        return Executors.newScheduledThreadPool(1);
    }
}
