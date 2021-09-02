package com.awesome.applications.scheduler;

import com.awesome.infrastructures.RedisCacheNotice;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
public class NoticeScheduler {
    private final RedisCacheNotice redisCacheNotice;

    /**
     * 매일 자정 Notice 동작
     */
    @Scheduled(cron="0 0 0 * * *")
    public void userYearAddition(){
        redisCacheNotice.setNotice();
    }
}
