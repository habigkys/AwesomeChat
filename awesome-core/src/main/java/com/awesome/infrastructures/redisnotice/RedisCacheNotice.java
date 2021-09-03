package com.awesome.infrastructures.redisnotice;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class RedisCacheNotice {
    @Autowired
    StringRedisTemplate redisTemplate;

    public String getNotice(){
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        return values.get("Notice");
    }

    public void setNotice(){
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        values.set("Notice", LocalDate.now().toString());
    }
}
