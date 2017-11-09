package com.navimee.configuration;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.configuration.specific.FirebaseConfiguration;
import com.navimee.configuration.specific.FoursquareConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
}
