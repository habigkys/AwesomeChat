package com.awesome.infrastructures.redisnotice.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@Entity
@RedisHash("notice")
public class Notice {
    @Id
    String id;

    private String noticeContents;
}
