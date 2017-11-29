package com.navimee.logger;

import com.google.cloud.firestore.Firestore;
import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.firestore.Paths;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    private static Firestore db = FirebaseInitialization.firestore;
    private static List<Log> list = new ArrayList<>();
    private static ObservableList<Log> observableList = FXCollections.observableList(list);

    static {
        observableList.addListener((ListChangeListener.Change<? extends Log> c) -> {
            while (c.next()) {
                for (Log added : c.getAddedSubList()) {
                    if (db != null)
                        try {
                            db.collection(Paths.LOGS).document().set(added).get();
                        } catch (Exception ignore) {
                            ignore.printStackTrace();
                        }
                }
            }
        });
    }

    public static void LOG(Log log) {
        observableList.add(log);
        if (observableList.size() > 10000)
            for (int i = 0; i < observableList.size(); i++)
                observableList.remove(i);
    }
}
