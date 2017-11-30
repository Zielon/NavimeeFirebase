package com.navimee.logger;

import com.google.cloud.firestore.Firestore;
import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.firestore.Paths;

import java.util.concurrent.Future;

public class Logger {

    private static Firestore db = FirebaseInitialization.firestore;

    public static Future LOG(Log log) {
        return db != null ? db.collection(Paths.LOGS).document().set(log) : null;
    }
}
