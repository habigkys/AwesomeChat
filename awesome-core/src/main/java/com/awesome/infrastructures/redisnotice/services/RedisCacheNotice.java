package com.awesome.infrastructures.redisnotice.services;

import com.awesome.infrastructures.redisnotice.entities.Notice;
import com.awesome.infrastructures.redisnotice.entities.NoticeDAO;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class RedisCacheNotice {
    private NoticeDAO noticeDAO;

    @Cacheable(value = "id", key = "#id")
    public String getNotice(){
        return noticeDAO.findAll().toString();
    }

    @Transactional
    public void setNotice(){
        Notice notice = new Notice();
        notice.setNoticeContents(LocalDateTime.now().toString());
        noticeDAO.save(notice);
    }
}
