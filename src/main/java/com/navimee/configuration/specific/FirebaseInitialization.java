package com.navimee.configuration.specific;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.configuration.Configuration;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseInitialization extends Configuration {

    private static Firestore firestore;
    private static FirebaseDatabase firebase;
    private JSONObject config;

    public FirebaseInitialization() throws IOException {
        super("", getConfigVar("FIREBASE_CLIENT_ID"), getConfigVar("FIREBASE_PRIVATE_KEY"), "");

        config = new JSONObject();

        config.put("private_key", clientSecret);
        config.put("client_id", clientId);
        config.put("private_key_id", getConfigVar("FIREBASE_PRIVATE_KEY_ID"));
        config.put("client_email", getConfigVar("FIREBASE_CLIENT_EMAIL"));
        config.put("type", getConfigVar("FIREBASE_TYPE"));
        config.put("project_id", getConfigVar("FIREBASE_PROJECT_ID"));

        InputStream inputStream = new ByteArrayInputStream(config.toString().getBytes());
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(googleCredentials)
                .setDatabaseUrl(getConfigVar("FIREBASE_URL"))
                .build();

        if (FirebaseApp.getApps().isEmpty())
            FirebaseApp.initializeApp(options);

        firestore = FirestoreClient.getFirestore();
        firebase = FirebaseDatabase.getInstance();
    }

    public Firestore getFirestore() {
        return firestore;
    }

    public FirebaseDatabase getFirebase() {
        return firebase;
    }
}
