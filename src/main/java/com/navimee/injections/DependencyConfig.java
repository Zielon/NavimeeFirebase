package com.navimee.injections;

import com.navimee.configuration.FacebookConfiguration;
import com.navimee.configuration.FirebaseConfiguration;
import com.navimee.configuration.FlightstatsConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class DependencyConfig {

    @Value(value = "classpath:google-services.json")
    private Resource firebaseConfig;

    @Value(value = "classpath:facebook-services.json")
    private Resource facebookConfig;

    @Value(value = "classpath:flightstats-services.json")
    private Resource flightstatsConfig;

    @Bean
    FirebaseConfiguration providerFirebaseConfiguration() throws IOException {
        return new FirebaseConfiguration(firebaseConfig);
    }

    @Bean
    FacebookConfiguration providerFacebookConfiguration() throws IOException {
        return new FacebookConfiguration(facebookConfig);
    }

    @Bean
    FlightstatsConfiguration providerFlightstatsConfiguration() throws IOException {
        return new FlightstatsConfiguration(flightstatsConfig);
    }
}
