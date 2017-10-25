package com.navimee;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class FirebaseConfiguration {
    private final JSONObject config;
    private final Resource firebaseConfig;

    public FirebaseConfiguration(Resource firebaseConfig) throws IOException {
        this.firebaseConfig = firebaseConfig;
        BufferedReader streamReader = new BufferedReader(
                new InputStreamReader(firebaseConfig.getInputStream(), "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) responseStrBuilder.append(inputStr);
        config = new JSONObject(responseStrBuilder.toString());
    }

    public JSONObject GetConfig(){
        return config;
    }

    public String GetAccessToken() throws IOException {
        GoogleCredential googleCred = GoogleCredential.fromStream(firebaseConfig.getInputStream());
        GoogleCredential scoped = googleCred.createScoped(
            Arrays.asList(
                    "https://www.googleapis.com/auth/firebase.database",
                    "https://www.googleapis.com/auth/userinfo.email"
            )
        );

        scoped.refreshToken();
        return scoped.getAccessToken();
    }
}
