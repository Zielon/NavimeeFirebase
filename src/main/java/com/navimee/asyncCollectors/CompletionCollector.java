package com.navimee.asyncCollectors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class CompletionCollector {

    public static <T> List<T> waitForSingleFuture(ExecutorService executorService, Collection<? extends Future<T>> futures) {
        List<T> output = new ArrayList<>();
        CompletionService<T> completionService = new ExecutorCompletionService<>(executorService);

        // Create callable tasks
        futures.forEach(future -> completionService.submit(() -> future.get()));

        int received = 0;
        while (received < futures.size()) {
            try {
                T result = completionService.take().get();
                if (result != null)
                    output.add(result);
                received++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return output;
    }

    public static <T> List<T> waitForFutures(ExecutorService executorService, Collection<? extends Future<List<T>>> futures) {
        List<T> output = new ArrayList<>();
        CompletionService<List<T>> completionService = new ExecutorCompletionService<>(executorService);

        // Create callable tasks
        futures.forEach(future -> completionService.submit(() -> future.get()));

        int received = 0;
        while (received < futures.size()) {
            try {
                List<T> result = completionService.take().get();
                if (result != null && result.size() > 0)
                    output.addAll(result);
                received++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return output;
    }

    public static <T> List<T> waitForSingleTask(ExecutorService executorService, Collection<Callable<T>> tasks) {
        List<T> output = new ArrayList<>();
        CompletionService<T> completionService = new ExecutorCompletionService<>(executorService);

        // Create callable tasks
        tasks.forEach(completionService::submit);

        int received = 0;
        while (received < tasks.size()) {
            try {
                T result = completionService.take().get();
                if (result != null)
                    output.add(result);
                received++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return output;
    }

    public static <T> List<T> waitForTasks(ExecutorService executorService, Collection<Callable<List<T>>> tasks) {
        List<T> output = new ArrayList<>();
        CompletionService<List<T>> completionService = new ExecutorCompletionService<>(executorService);

        // Create callable tasks
        tasks.forEach(completionService::submit);

        int received = 0;
        while (received < tasks.size()) {
            try {
                List<T> result = completionService.take().get();
                if (result != null && result.size() > 0)
                    output.addAll(result);
                received++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return output;
    }
}
