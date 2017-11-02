package com.navimee.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;

public class FirebaseConfiguration extends Configuration {

    private final JSONObject config;
    private final Resource firebaseConfig;

    public final String accessToken;

    public FirebaseConfiguration(Resource firebaseConfig) throws IOException {
        this.firebaseConfig = firebaseConfig;
        config = transformConfig(firebaseConfig);
        accessToken = getAccessToken();
    }

    private String getAccessToken() {
        GoogleCredential googleCred = null;
        try {
            googleCred = GoogleCredential.fromStream(firebaseConfig.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        GoogleCredential scoped = googleCred.createScoped(
                Arrays.asList(
                        "https://www.googleapis.com/auth/firebase.database",
                        "https://www.googleapis.com/auth/userinfo.email"
                )
        );

        try {
            scoped.refreshToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scoped.getAccessToken();
    }
}
