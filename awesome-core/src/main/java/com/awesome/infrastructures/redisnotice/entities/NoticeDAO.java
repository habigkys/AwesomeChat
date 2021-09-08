package com.awesome.infrastructures.redisnotice.entities;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface NoticeDAO extends CrudRepository<Notice, Long> {
    Optional<Notice> findByNoticedDate(String noticedDate);
}
