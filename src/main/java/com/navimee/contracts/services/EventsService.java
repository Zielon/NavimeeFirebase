package com.navimee.contracts.services;

import java.util.concurrent.Future;

public interface EventsService {
    Future saveFacebookEvents(String city);
}