package com.navimee.contracts.services;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public interface HttpClient {
    <T> Future<T> getFromFirebase(TypeReference<T> type, String uri);
}
