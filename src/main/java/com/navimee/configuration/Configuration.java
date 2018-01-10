package com.navimee.configuration;

import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class Configuration {

    protected String accessToken;
    protected String clientId;
    protected String clientSecret;
    private String apiUrl;

    protected Configuration(String accessToken, String clientId, String clientSecret, String apiUrl) {
        this.accessToken = accessToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.apiUrl = apiUrl;
    }

    protected static String getConfigVar(String key) {
        return System.getenv().get(key.toUpperCase()).replace("\\n", "\n");
    }

    protected JSONObject transformConfig(Resource configuration) throws IOException {

        BufferedReader streamReader = new BufferedReader(
                new InputStreamReader(configuration.getInputStream(), "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) responseStrBuilder.append(inputStr);

        return new JSONObject(responseStrBuilder.toString());
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
