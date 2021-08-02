package com.awesome.applications.scheduler;

import com.awesome.domains.user.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@AllArgsConstructor
public class UserScheduler {
    private final UserService userService;

    /**
     * 매년 연말에 연차 + 1
     */
    @Scheduled(cron="0 0 0 31 12 ?")
    public void userYearAddition(){
        userService.updateUserYear();
    }
}
