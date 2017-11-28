package com.navimee.firestore.operations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Base {
    protected static ExecutorService executorService = Executors.newFixedThreadPool(2);
}
