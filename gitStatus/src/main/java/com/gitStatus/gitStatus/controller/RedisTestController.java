package com.gitStatus.gitStatus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class RedisTestController {

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/redis")
    public String testRedis() {

        redisTemplate
                .opsForValue()
                .set(
                        "hello",
                        "world"
                );

        return (String)
                redisTemplate
                        .opsForValue()
                        .get(
                                "hello"
                        );
    }
}