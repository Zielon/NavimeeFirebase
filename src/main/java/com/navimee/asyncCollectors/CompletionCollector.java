package com.navimee.asyncCollectors;

import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class CompletionCollector {

    public static <T> List<T> waitForFutures(ExecutorService executorService, List<? extends Future<T>> futures) {
        CompletionService<T> completionService = new ExecutorCompletionService<>(executorService);
        futures.forEach(future -> completionService.submit(() -> future.get()));
        return collect(completionService, futures.size(), List::add);
    }

    public static <T> List<T> waitForSingleTask(ExecutorService executorService, List<Callable<T>> tasks) {
        CompletionService<T> completionService = new ExecutorCompletionService<>(executorService);
        tasks.forEach(completionService::submit);
        return collect(completionService, tasks.size(), List::add);
    }

    public static <T> List<T> waitForManyTasks(ExecutorService executorService, List<Callable<List<T>>> tasks) {
        CompletionService<List<T>> completionService = new ExecutorCompletionService<>(executorService);
        tasks.forEach(completionService::submit);
        return collect(completionService, tasks.size(), List::add).stream().flatMap(List::stream).collect(toList());
    }

    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.<T>toList())
        );
    }

    private static <T> List<T> collect(CompletionService<T> completionService, int size, BiConsumer<List<T>, T> consumer) {
        List<T> output = new ArrayList<>();
        int received = 0;
        while (received < size) {
            try {
                T result = completionService.take().get();
                if (result != null)
                    consumer.accept(output, result);
                received++;
            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            }
        }
        return output;
    }
}
