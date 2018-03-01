package com.navimee.logger;

import com.google.cloud.firestore.Firestore;
import com.navimee.firestore.FirebasePaths;
import com.navimee.models.entities.Log;

import java.util.concurrent.Future;

public class Logger {

    private static boolean isRunning = true;
    private static Firestore db;

    public static void setDb(Firestore db) {
        if (Logger.db == null)
            Logger.db = db;
    }

    public static void setRunning(boolean isRunning) {
        Logger.isRunning = isRunning;
    }

    public static synchronized Future LOG(Log log) {
        if (isRunning)
            return db != null ? db.collection(FirebasePaths.LOGS).document().set(log) : null;
        else return null;
    }
}
