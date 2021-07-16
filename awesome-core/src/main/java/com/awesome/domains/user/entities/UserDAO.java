package com.awesome.domains.user.entities;

import com.awesome.domains.project.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<UserEntity, Long> {
}
