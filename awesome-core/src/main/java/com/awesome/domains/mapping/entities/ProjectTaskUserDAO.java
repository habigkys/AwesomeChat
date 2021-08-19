package com.awesome.domains.mapping.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectTaskUserDAO extends JpaRepository<ProjectTaskUserEntity, Long> {
    List<ProjectTaskUserEntity> findAllByTaskId(Long taskId);
    List<ProjectTaskUserEntity> findAllByUserId(Long userId);
    void deleteAllByTaskId(Long taskId);
}
