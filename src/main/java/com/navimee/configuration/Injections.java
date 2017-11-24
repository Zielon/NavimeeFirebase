package com.navimee.configuration;

import com.google.cloud.firestore.Firestore;
import com.navimee.configuration.specific.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class Injections {

    @Value(value = "classpath:google-services.json")
    private Resource firebaseConfig;

    @Value(value = "classpath:facebook-services.json")
    private Resource facebookConfig;

    @Value(value = "classpath:foursquare-services.json")
    private Resource foursquareConfig;

    @Value(value = "classpath:google-geo-services.json")
    private Resource googleGeoConfig;

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
        return new GoogleConfiguration(googleGeoConfig);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modalMapper = new ModelMapper();

/*        modalMapper.addConverter((MappingContext<String, DateTime> context) -> {
            if(context.getSource() == null) return null;
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
            return formatter.parseDateTime(context.getSource().toString());
        });*/

        return modalMapper;
    }

    @Bean
    @Scope("singleton")
    public Firestore provideFirestore() {
        return FirebaseInitialization.getDatabaseReference(firebaseConfig);
    }
}
