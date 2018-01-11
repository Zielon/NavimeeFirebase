package com.navimee.configuration;

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
