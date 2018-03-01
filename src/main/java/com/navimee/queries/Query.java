package com.navimee.queries;

import com.navimee.configuration.Configuration;
import com.navimee.contracts.services.HttpClient;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public abstract class Query<T, C extends Configuration, P extends QueryParams> {

    protected C configuration;
    protected ExecutorService executorService;
    protected HttpClient httpClient;

    public Query(C configuration, ExecutorService executorService, HttpClient httpClient) {
        this.configuration = configuration;
        this.executorService = executorService;
        this.httpClient = httpClient;
    }

    public abstract CompletableFuture<T> execute(P params);

    protected abstract T map(CompletableFuture<JSONObject> object, P params);
}
