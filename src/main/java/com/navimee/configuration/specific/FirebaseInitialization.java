package com.navimee.configuration.specific;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseInitialization {

    public static Firestore firestore = null;
    public static FirebaseDatabase firebase = null;

    private static void initialize(Resource firebaseConfig) {
        if (firestore == null || firebase == null) {
            FirebaseOptions options = null;
            try {
                InputStream serviceAccount = new FileInputStream(firebaseConfig.getFile());
                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
                options = new FirebaseOptions.Builder()
                        .setCredentials(credentials)
                        .setDatabaseUrl("https://navimee-1a213.firebaseio.com/")
                        .build();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FirebaseApp.initializeApp(options);

            firestore = FirestoreClient.getFirestore();
            firebase = FirebaseDatabase.getInstance();
        }
    }

    public static FirebaseDatabase getFirebaseReference(Resource firebaseConfig) {
        if (firebase == null)
            initialize(firebaseConfig);
        return firebase;
    }

    public static Firestore getFirestoreReference(Resource firebaseConfig) {
        if (firestore == null)
            initialize(firebaseConfig);
        return firestore;
    }
}
