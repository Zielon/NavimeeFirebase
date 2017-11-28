package com.navimee.contracts.services;

import org.json.JSONObject;

import java.net.URI;
import java.util.concurrent.Callable;

public interface HttpClient {
    Callable<JSONObject> GET(URI uri);

    void close();
}
