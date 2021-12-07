package com.awesome.infrastructures.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@AllArgsConstructor
@Component
public class RedisClient {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper redisObjectMapper;

    /**
     * @param key must not be null
     * @param value must not be null
     * @param expire must be more than zero
     */
    public void set(String key, String value, long expire) {
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofMillis(expire));
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public <E> void publish(ChannelTopic topic, E body) throws Exception {
        String bodyString = redisObjectMapper.writeValueAsString(body);

        stringRedisTemplate.convertAndSend(topic.getTopic(), bodyString);
    }
}
