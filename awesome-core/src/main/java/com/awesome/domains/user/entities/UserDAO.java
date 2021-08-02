package com.awesome.domains.user.entities;

import com.awesome.domains.project.entities.ProjectEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByUserNameLike(String userName);
}
