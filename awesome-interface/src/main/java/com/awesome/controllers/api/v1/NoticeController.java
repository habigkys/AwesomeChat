package com.awesome.controllers.api.v1;


import com.awesome.infrastructures.redisnotice.services.RedisCacheNotice;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/notice")
public class NoticeController {
    private final RedisCacheNotice redisCacheNotice;

    /**
     * 1. Get Notice
     * @return
     */
    @GetMapping("/")
    public String getNotice() {
        return redisCacheNotice.getNotice(LocalDate.now().toString());
    }
}