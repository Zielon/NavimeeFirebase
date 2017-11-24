package com.navimee.contracts.services.events;

public interface EventsService {
    void saveFacebookEvents(String city);

    void saveSevenDaysSegregation(String city);
}