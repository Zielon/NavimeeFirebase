package com.navimee.logger;

import com.google.cloud.firestore.Firestore;
import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.firestore.Paths;
import com.navimee.models.entities.Log;

import java.util.concurrent.Future;

public class Logger {

    public static boolean isRunning = true;

    private static Firestore db = FirebaseInitialization.firestore;

    public static synchronized Future LOG(Log log) {
        if (isRunning)
            return db != null ? db.collection(Paths.LOGS).document().set(log) : null;
        else return null;
    }
}
