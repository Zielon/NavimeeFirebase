package com.navimee.contracts.services;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import java.net.URI;
import java.util.concurrent.Callable;

public interface HttpClient {
    Callable<JSONObject> GET(URI uri);

    Callable<JSONObject> GET(HttpGet httpGet);

    void close();
}
