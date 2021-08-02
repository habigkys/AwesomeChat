package com.awesome.domains.user.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByUserNameLike(String userName);
}
