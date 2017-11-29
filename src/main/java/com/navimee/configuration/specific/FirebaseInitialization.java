package com.navimee.configuration.specific;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseInitialization {

    public static Firestore firestore = null;

    // Initialize the Firebase instance only once.
    public static Firestore getDatabaseReference(Resource firebaseConfig) {

        if (firestore == null) {
            FirebaseOptions options = null;
            try {
                InputStream serviceAccount = new FileInputStream(firebaseConfig.getFile());
                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
                options = new FirebaseOptions.Builder()
                        .setCredentials(credentials)
                        .build();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FirebaseApp.initializeApp(options);
            firestore = FirestoreClient.getFirestore();
        }

        return firestore;
    }
}
