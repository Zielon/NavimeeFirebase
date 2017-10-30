package com.navimee.injections;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.configuration.FacebookConfiguration;
import com.navimee.configuration.FirebaseConfiguration;
import com.navimee.configuration.FlightstatsConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class DependencyConfig {

    @Value(value = "classpath:google-services.json")
    private Resource firebaseConfig;

    @Value(value = "classpath:facebook-services.json")
    private Resource facebookConfig;

    @Value(value = "classpath:flightstats-services.json")
    private Resource flightstatsConfig;

    @Value("${firebase.database-url}")
    private String databaseUrl;



    @Bean
    FirebaseConfiguration providerFirebaseConfiguration() throws IOException {
        return new FirebaseConfiguration(firebaseConfig);
    }

    @Bean
    FacebookConfiguration providerFacebookConfiguration() throws IOException {
        return new FacebookConfiguration(facebookConfig);
    }

    @Bean
    FlightstatsConfiguration providerFlightstatsConfiguration () throws IOException {
        return new FlightstatsConfiguration(flightstatsConfig);
    }
}
