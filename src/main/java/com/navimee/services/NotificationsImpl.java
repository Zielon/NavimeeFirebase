package com.navimee.services;

import com.navimee.contracts.services.Notifications;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class NotificationsImpl implements Notifications {
    @Override
    public Future send(String userToken) {
        return null;
    }
}
