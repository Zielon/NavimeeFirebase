package com.navimee.services;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.FirebaseConfiguration;
import com.navimee.contracts.services.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class HttpClientImpl implements HttpClient {

    @Value("${firebase.database-url}")
    private String databaseUrl;

    @Autowired
    FirebaseConfiguration firebaseConfiguration;

    @Override
    public <T> List<T> GetAsync(String child) throws IOException, UnirestException {

        JsonNode json = Unirest.get(String.format("%s/{child}{end}", databaseUrl))
                .header("accept", "application/json")
                .routeParam("child", child)
                .routeParam("end", ".json")
                .queryString("access_token", firebaseConfiguration.GetAccessToken())
                .asJson().getBody();

        return null;
    }
}
