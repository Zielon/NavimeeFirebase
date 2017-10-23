package com.navimee.navimee.service;

/**
 * Created by Wojtek on 2017-10-23.
 */
public interface FirebaseService {

    /**
     * Open connection with Firebase and start listening for events
     */
    public void startFirebaseListener();

    public void saveEvents();

}