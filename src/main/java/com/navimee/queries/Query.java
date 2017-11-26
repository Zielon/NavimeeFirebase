package com.navimee.queries;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.navimee.configuration.Configuration;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;

public abstract class Query<T, C extends Configuration, P extends QueryParams> {

    protected C configuration;

    protected ExecutorService executorService;

    public Query(C configuration, ExecutorService executorService) {
        this.configuration = configuration;
        this.executorService = executorService;
    }

    public abstract Future<T> execute(P params);

    protected abstract T map(Future<HttpResponse<JsonNode>> object);
}
