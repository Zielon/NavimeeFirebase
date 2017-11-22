package com.navimee.contracts.services.events;

import com.navimee.models.bussinesObjects.events.FbEventBo;

import java.util.List;
import java.util.Map;

public interface EventsService {
    List<FbEventBo> downloadFacebookEvents(String city);

    Map<String, List<FbEventBo>> sevenDaysSegregation(String city);
}