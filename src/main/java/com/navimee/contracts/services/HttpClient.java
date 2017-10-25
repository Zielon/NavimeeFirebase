package com.navimee.contracts.services;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.List;

public interface HttpClient {
    <T> List<T> GetAsync(Class<T> type, String uri) throws IOException, UnirestException;
}
