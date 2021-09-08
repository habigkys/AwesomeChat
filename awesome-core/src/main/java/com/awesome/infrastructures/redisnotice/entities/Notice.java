package com.awesome.infrastructures.redisnotice.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import java.time.LocalDate;

@Getter
@Setter
@Cacheable
@RedisHash("notice")
public class Notice {
    @Id
    private Long noticeId;

    @Indexed
    private String noticedDate;

    private String noticeContents;
}
