package com.navimee.contracts.services.events;

import java.util.concurrent.Future;

public interface EventsService {
    Future saveFacebookEvents(String city);
}