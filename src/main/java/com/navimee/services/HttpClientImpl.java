package com.navimee.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.specific.FirebaseConfiguration;
import com.navimee.contracts.services.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class HttpClientImpl implements HttpClient {

    @Value("${firebase.database-url}")
    private String databaseUrl;

    @Autowired
    FirebaseConfiguration firebaseConfiguration;

    @Override
    public <T> Future<T> getFromFirestore(TypeReference<T> type, String child) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<HttpResponse<JsonNode>> response = Unirest.get(String.format("%s/{child}{end}", databaseUrl))
                .header("accept", "application/json")
                .routeParam("child", child)
                .routeParam("end", ".json")
                .queryString("access_token", firebaseConfiguration.accessToken)
                .asJsonAsync();

        return executor.submit(() -> map(response.get().getBody(), type));
    }

    private <T> T map(JsonNode json, TypeReference<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        T output = null;
        try {
            output = mapper.readValue(json.toString(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
