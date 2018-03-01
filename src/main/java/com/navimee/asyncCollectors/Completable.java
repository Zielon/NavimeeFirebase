package com.navimee.asyncCollectors;

import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Completable {

    public static <T> List<T> wait(ExecutorService executorService, List<? extends Future<T>> futures) {
        CompletionService<T> completionService = new ExecutorCompletionService<>(executorService);
        futures.forEach(future -> completionService.submit(() -> future.get()));
        return collect(completionService, futures.size(), List::add);
    }

    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.<T>toList())
        );
    }

    private static <T> List<T> collect(CompletionService<T> completionService, int size, BiConsumer<List<T>, T> consumer) {
        List<T> output = new ArrayList<>();
        int received = 0;
        while (received < size) {
            try {
                Future<T> future = completionService.take();
                T result = future.get();
                if (result != null)
                    consumer.accept(output, result);
            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            }
            received++;
        }
        return output;
    }
}
