package com.awesome.domains.projecttask.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectTaskDAO extends JpaRepository<ProjectTaskEntity, Long> {
    List<ProjectTaskEntity> findAllByProjectId(Long projectId);
}
