package com.navimee.asyncCollectors;

import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class CompletionCollector {

    public static <T> List<T> waitForFutures(ExecutorService executorService, Collection<? extends Future<T>> futures) {
        CompletionService<T> completionService = new ExecutorCompletionService<>(executorService);
        futures.forEach(future -> completionService.submit(() -> future.get()));
        return collectSingle(completionService, futures.size());
    }

    public static void waitForFutures(ExecutorService executorService, List<Future> futures) {
        CompletionService<Object> completionService = new ExecutorCompletionService<>(executorService);
        futures.forEach(future -> completionService.submit(() -> future.get()));
        collectSingle(completionService, futures.size());
    }

    public static <T> List<T> waitForSingleTask(ExecutorService executorService, Collection<Callable<T>> tasks) {
        CompletionService<T> completionService = new ExecutorCompletionService<>(executorService);
        tasks.forEach(completionService::submit);
        return collectSingle(completionService, tasks.size());
    }

    public static <T> List<T> waitForTasks(ExecutorService executorService, Collection<Callable<List<T>>> tasks) {
        CompletionService<List<T>> completionService = new ExecutorCompletionService<>(executorService);
        tasks.forEach(completionService::submit);
        return collectLists(completionService, tasks.size());
    }

    private static <T> List<T> collectSingle(CompletionService<T> completionService, int size) {
        List<T> output = new ArrayList<>();
        int received = 0;
        while (received < size) {
            try {
                T result = completionService.take().get();
                if (result != null)
                    output.add(result);
                received++;
            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            }
        }
        return output;
    }

    private static <T> List<T> collectLists(CompletionService<List<T>> completionService, int size) {
        List<T> output = new ArrayList<>();
        int received = 0;
        while (received < size) {
            try {
                List<T> result = completionService.take().get();
                if (result != null && result.size() > 0)
                    output.addAll(result);
                received++;
            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            }
        }
        return output;
    }
}
