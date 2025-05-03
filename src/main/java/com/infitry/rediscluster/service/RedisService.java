package com.infitry.rediscluster.service;

import com.infitry.rediscluster.aop.Timer;
import com.infitry.rediscluster.model.Tester;
import com.infitry.rediscluster.repository.RedisRepository;
import com.infitry.rediscluster.service.factory.TesterFactory;
import com.infitry.rediscluster.util.AsyncUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private final ThreadPoolTaskExecutor threadPoolExecutor;
    private final RedisRepository redisRepository;

    public Object get(String key) {
        return redisRepository.get(key);
    }

    @Timer
    public void batchSet() {
        var testers = TesterFactory.createTesters();
        var futures = new ArrayList<CompletableFuture<Void>>();

        ListUtils.partition(testers, 1000).forEach(list -> {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                var saveFutures = new ArrayList<CompletableFuture<Void>>();
                list.forEach(tester -> {
                    saveFutures.add(saveTesterToRedis(tester));
                });
                AsyncUtil.waitForAll(saveFutures);
            }, threadPoolExecutor);
            futures.add(future);
        });

        AsyncUtil.waitForAll(futures);
    }

    private CompletableFuture<Void> saveTesterToRedis(Tester tester) {
        return CompletableFuture.runAsync(() -> {
            redisRepository.set(tester.getId().toString(), tester);
        }, threadPoolExecutor);
    }
}
