package com.navimee.logger;

import com.google.cloud.firestore.Firestore;
import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.firestore.Paths;
import com.navimee.models.entities.Log;

import java.util.concurrent.Future;

public class Logger {

    public static boolean IsRunning = true;

    private static Firestore db = FirebaseInitialization.firestore;

    public static Future LOG(Log log) {
        if (IsRunning)
            return db != null ? db.collection(Paths.LOGS).document().set(log) : null;
        else return null;
    }
}
