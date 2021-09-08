package com.awesome.infrastructures.redisnotice.services;

import com.awesome.infrastructures.exceptions.AwesomeException;
import com.awesome.infrastructures.exceptions.AwesomeExceptionType;
import com.awesome.infrastructures.redisnotice.entities.Notice;
import com.awesome.infrastructures.redisnotice.entities.NoticeDAO;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RedisCacheNotice {
    private NoticeDAO noticeDAO;

    @Cacheable(value = "noticeCache", key = "#noticedDate", unless = "#result == null", cacheManager = "redisNoticeCacheManager")
    public String getNotice(String noticedDate){
        Optional<Notice> byId = noticeDAO.findByNoticedDate(noticedDate);

        if(byId.isEmpty()){
            // 공지 없음.
            return null;
        }

        Notice notice = byId.get();

        return notice.getNoticeContents();
    }

    @Transactional
    @CachePut(value = "noticeCache", key = "#noticedDate", cacheManager = "redisNoticeCacheManager")
    public void setNotice(String noticedDate){
        Notice notice = new Notice();
        notice.setNoticedDate(LocalDate.now().toString());
        notice.setNoticeContents(LocalDate.now().toString());
        noticeDAO.save(notice);
    }
}
