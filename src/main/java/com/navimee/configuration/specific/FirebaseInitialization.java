package com.navimee.configuration.specific;

import com.navimee.NavimeeApplication;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.*;

public class FirebaseInitialization {

    private static FirebaseApp firebaseApp;

    // Initialize the Firebase instance only once.
    public static Firestore getDatabaseReference() {
        synchronized (FirebaseInitialization.class) {
            if (firebaseApp == null) {
                InputStream serviceAccount = null;
                try {
                    ClassLoader classLoader = NavimeeApplication.class.getClassLoader();
                    File configFile = new File(classLoader.getResource("google-services.json").getFile());
                    serviceAccount = new FileInputStream(configFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                FirebaseOptions options = null;

                try {
                    GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
                    options = new FirebaseOptions.Builder()
                            .setCredentials(credentials)
                            .build();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                firebaseApp = FirebaseApp.initializeApp(options, "Navimee");
            }

            return FirestoreClient.getFirestore();
        }
    }
}
