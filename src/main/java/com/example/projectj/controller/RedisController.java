package com.example.projectj.controller;

import com.example.projectj.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.java.Log;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Log
@RestController
public class RedisController {
    @Cacheable(cacheNames = "projectJ", key = "'myKey'")
    @GetMapping("/test/redis")
    public String redisTest() throws JsonProcessingException {
        List<Map<String, Object>> result = new ArrayList<>();

        for(int i=1; i < 100; i++) {
            Map<String, Object> re = new HashMap<>();
            Random random = new Random();
            int ra = random.nextInt();
            re.put("id", i);
            re.put("random", ra);
            result.add(re);
        }

        return CommonUtil.objectToJsonString(result);
    }

    @Cacheable(cacheNames = "redis")
    @GetMapping("/test/redis1")
    public List<Map<String, Object>> redisTest1() {
        return null;
    }

    @CacheEvict(cacheNames = "redis", allEntries = true)
    @GetMapping("/test/redis2")
    public List<Map<String, Object>> redisTest2() {
        return null;
    }
}
