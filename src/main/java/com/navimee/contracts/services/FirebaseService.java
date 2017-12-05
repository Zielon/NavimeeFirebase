package com.navimee.contracts.services;

import com.navimee.models.entities.events.FbEvent;

import java.util.List;
import java.util.concurrent.Future;

public interface FirebaseService {
    Future transferEvents(List<FbEvent> events);
}
