package com.navimee.asyncCollectors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class HelperMethods {

    public static <T> List<T> waitForMany(List<Future<List<T>>> futures) {
        List<T> output = new ArrayList<>();
        futures.parallelStream().forEach(future -> {
            try {
                List<T> result = future.get();
                if(result != null && result.size() > 0)
                    output.addAll(result);
            } catch (Exception e) {
            }
        });

        return output;
    }

    public static <T> List<T> waitForAll(List<Future<T>> futures) {
        List<T> output = new ArrayList<>();
        futures.parallelStream().forEach(future -> {
            try {
                output.add(future.get());
            } catch (Exception e) {
            }
        });

        return output;
    }
}
