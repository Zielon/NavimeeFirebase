package com.navimee.asynchronous;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Task {

    public static <T> List<T> waitForAll(List<Future<List<T>>> futures) {
        List<T> output = new ArrayList<>();
        for (Future<List<T>> future : futures) {
            try {
                output.addAll(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return output;
    }
}
