package com.navimee.asyncCollectors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HelperMethods {

    public static <T> List<T> waitForMany(List<Future<List<T>>> futures) {
        List<T> output = new ArrayList<>();
        futures.parallelStream().forEach(future -> {
            try {
                List<T> result = future.get();
                if (result != null && result.size() > 0)
                    output.addAll(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return output;
    }

    public static <T> List<T> waitForAll(List<Future<T>> futures) {
        List<T> output = new ArrayList<>();
        futures.parallelStream().forEach(future -> {
            try {
                T result = future.get();
                if (result != null)
                    output.add(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return output;
    }

    public static <T> T waitForSingle(Future<T> future) {
        T output = null;
        try {
            output = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return output;
    }
}
