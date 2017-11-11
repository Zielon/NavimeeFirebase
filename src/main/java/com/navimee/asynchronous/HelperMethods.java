package com.navimee.asynchronous;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class HelperMethods {

    public static <T> List<T> waitForAll(List<Future<List<T>>> futures) {
        List<T> output = new ArrayList<>();
        futures.parallelStream().forEach(future -> {
            try {
                output.addAll(future.get());
            } catch (Exception e) {}
        });

        return output;
    }
}
