package com.navimee.contracts.services;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

public interface HttpClient {

    CompletableFuture<JSONObject> GET(URI uri);

    CompletableFuture<JSONObject> GET(HttpGet httpGet);

    void close();
}
