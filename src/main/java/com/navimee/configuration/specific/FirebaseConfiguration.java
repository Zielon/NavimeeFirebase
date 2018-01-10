package com.navimee.configuration.specific;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.navimee.configuration.Configuration;
import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class FirebaseConfiguration extends Configuration {

    private JSONObject config;

    public FirebaseConfiguration(Resource firebaseConfig) throws IOException {
        super("", getConfigVar("FIREBASE_CLIENT_ID"), getConfigVar("FIREBASE_PRIVATE_KEY"), "");

        config = transformConfig(firebaseConfig);

        config.put("private_key", clientSecret);
        config.put("client_id", clientId);
        config.put("private_key_id", getConfigVar("FIREBASE_PRIVATE_KEY_ID"));
        config.put("client_email", getConfigVar("FIREBASE_CLIENT_EMAIL"));

        accessToken = generateAccessToken();
    }

    private String generateAccessToken() throws IOException {
        GoogleCredential googleCredential = GoogleCredential.fromStream(new ByteArrayInputStream(config.toString().getBytes()));

        GoogleCredential scoped =
                googleCredential.createScoped(Arrays.asList(
                        "https://www.googleapis.com/auth/firebase.database",
                        "https://www.googleapis.com/auth/userinfo.email"
                ));

        scoped.refreshToken();

        return scoped.getAccessToken();
    }
}
