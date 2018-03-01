package com.navimee.contracts.services;

import com.navimee.models.entities.contracts.FcmSendable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Function;

public interface FcmService {

    <T extends FcmSendable> Future send(List<T> sendables, Function<T, Map<String, Object>> function);
}