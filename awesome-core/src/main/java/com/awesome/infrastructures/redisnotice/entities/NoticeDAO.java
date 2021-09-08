package com.awesome.infrastructures.redisnotice.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NoticeDAO extends CrudRepository<Notice, Long> {
}
