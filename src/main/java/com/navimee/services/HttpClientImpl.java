package com.navimee.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.configuration.FirebaseConfiguration;
import com.navimee.contracts.services.HttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HttpClientImpl implements HttpClient {

    @Value("${firebase.database-url}")
    private String databaseUrl;

    @Autowired
    FirebaseConfiguration firebaseConfiguration;

    @Override
    public <T> List<T> getFromFirebase(Class<T> type, String child) throws IOException, UnirestException {

        JsonNode json = Unirest.get(String.format("%s/{child}{end}", databaseUrl))
                .header("accept", "application/json")
                .routeParam("child", child)
                .routeParam("end", ".json")
                .queryString("access_token", firebaseConfiguration.getAccessToken())
                .asJson()
                .getBody();

        ObjectMapper mapper = new ObjectMapper();
        JSONObject object = json.getObject();
        List<T> list = new ArrayList<>();
        for(Object obj : object.keySet()){
            String key = obj.toString();
            T mapped = mapper.readValue(object.get(key).toString(), type);
            list.add(mapped);
        }
        return list;
    }
}
