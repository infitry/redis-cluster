package com.infitry.rediscluster.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class AsyncUtil {
    public void waitForAll(List<CompletableFuture<Void>> futures) {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}
