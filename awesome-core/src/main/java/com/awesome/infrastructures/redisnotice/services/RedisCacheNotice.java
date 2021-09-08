package com.awesome.infrastructures.redisnotice.services;

import com.awesome.infrastructures.exceptions.AwesomeException;
import com.awesome.infrastructures.exceptions.AwesomeExceptionType;
import com.awesome.infrastructures.redisnotice.entities.Notice;
import com.awesome.infrastructures.redisnotice.entities.NoticeDAO;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RedisCacheNotice {
    private NoticeDAO noticeDAO;

    @Cacheable(key = "#id", value = "noticeCache")
    public String getNotice(Long id){
        Optional<Notice> byId = noticeDAO.findById(id);

        if(byId.isEmpty()){
            //throw new AwesomeException();
        }

        Notice notice = byId.get();

        return notice.getNoticeContents();
    }

    @Transactional
    public void setNotice(){
        Notice notice = new Notice();
        notice.setNoticeContents(LocalDateTime.now().toString());
        noticeDAO.save(notice);
    }
}
