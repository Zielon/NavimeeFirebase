package com.navimee.asyncCollectors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CompletionCollector {

    public static <T> List<T> waitForSingle(List<? extends Future<T>> futures) {
        List<T> output = new ArrayList<>();
        CompletionService<T> completionService = new ExecutorCompletionService<>(Executors.newFixedThreadPool(2));
        futures.forEach(future -> completionService.submit(() -> future.get()));

        int received = 0;
        while (received < futures.size()) {
            try {
                T result = completionService.take().get();
                output.add(result);
                received++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return output;
    }

    public static <T> List<T> waitForMany(List<? extends Future<List<T>>> futures) {
        List<T> output = new ArrayList<>();
        CompletionService<List<T>> completionService = new ExecutorCompletionService<>(Executors.newFixedThreadPool(2));
        futures.forEach(future -> completionService.submit(() -> future.get()));

        int received = 0;
        while (received < futures.size()) {
            try {
                List<T> result = completionService.take().get();
                if (result.size() > 0)
                    output.addAll(result);
                received++;
            } catch (Exception e) {
               e.printStackTrace();
            }
        }

        return output;
    }
}
