package com.navimee.contracts.services;

import java.util.concurrent.Future;

public interface NotificationsService {
    Future sendDaySchedule();

    void listenToFeedback();
}
