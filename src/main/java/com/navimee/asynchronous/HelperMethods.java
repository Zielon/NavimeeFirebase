package com.navimee.asynchronous;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HelperMethods {

    public static <T> List<T> waitForAll(List<Future<List<T>>> futures) {
        List<T> output = new ArrayList<>();
        futures.parallelStream().forEach(future -> {
            try {
                output.addAll(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        return output;
    }
}
