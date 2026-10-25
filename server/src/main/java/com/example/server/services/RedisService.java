package com.example.server.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private RedisTemplate redisTemplate;

    public RedisService (RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String get (String key) {
        String value = (String) redisTemplate.opsForValue().get(key);
        return value;
    }

    public void set (String key, String value, Long expireTime) {
        redisTemplate.opsForValue().set(key, value,  expireTime, TimeUnit.MINUTES);
    }

}
