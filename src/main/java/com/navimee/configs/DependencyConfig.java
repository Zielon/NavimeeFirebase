package com.navimee.configs;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.FirebaseConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class DependencyConfig {

    @Value(value = "classpath:google-services.json")
    private Resource gservicesConfig;

    @Value("${firebase.database-url}")
    private String databaseUrl;

    @Bean
    public FirebaseApp provideFirebaseOptions() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(gservicesConfig.getFile());
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                .setDatabaseUrl(databaseUrl)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    FirebaseConfiguration providerFirebaseConfiguration() throws IOException {
        return new FirebaseConfiguration(gservicesConfig);
    }

    @Bean
    @Qualifier("dbContext")
    public DatabaseReference provideDatabaseReference(FirebaseApp firebaseApp) {
        FirebaseDatabase
                .getInstance(firebaseApp)
                .setPersistenceEnabled(false);
        return FirebaseDatabase
                .getInstance(firebaseApp)
                .getReference();
    }
}
