package com.awesome.applications.scheduler;

import com.awesome.infrastructures.redisnotice.services.RedisCacheNotice;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

@AllArgsConstructor
public class NoticeScheduler {
    private final RedisCacheNotice redisCacheNotice;

    /**
     * 매일 자정 Notice 동작
     */
    @Scheduled(cron="0 0 0 * * *")
    public void setRedisCacheNotice(){
        redisCacheNotice.setNotice(LocalDate.now().toString());
    }
}
