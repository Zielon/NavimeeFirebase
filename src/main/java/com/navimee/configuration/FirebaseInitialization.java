package com.navimee.configuration;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.NavimeeApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FirebaseInitialization {

    private static FirebaseApp firebaseApp;

    // Initialize the Firebase instance only once.
    public static DatabaseReference getDatabaseReference() {
        synchronized (FirebaseInitialization.class) {
            if (firebaseApp == null) {
                FileInputStream serviceAccount = null;
                try {
                    ClassLoader classLoader = NavimeeApplication.class.getClassLoader();
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

                firebaseApp = FirebaseApp.initializeApp(options, "Navimee");
                FirebaseDatabase.getInstance(firebaseApp).setPersistenceEnabled(false);
            }

            return FirebaseDatabase.getInstance(firebaseApp).getReference();
        }
    }
}
