package com.navimee.contracts.services.events;

import com.navimee.contracts.models.bussinesObjects.Event;
import com.navimee.contracts.models.dataTransferObjects.places.PlaceDto;

import java.util.List;

public interface EventsService {
    List<Event> getFacebookEvents(List<PlaceDto> places);
}