package com.infitry.rediscluster.controller;

import com.infitry.rediscluster.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis/test")
@RequiredArgsConstructor
public class RedisTestController {
    private final RedisService redisService;

    @GetMapping("/string/{id}")
    public Object getString(@PathVariable String id) {
        return redisService.get(id);
    }

    @GetMapping("/string/batch")
    public void saveBulkString() {
        redisService.batchSet();
    }
}
