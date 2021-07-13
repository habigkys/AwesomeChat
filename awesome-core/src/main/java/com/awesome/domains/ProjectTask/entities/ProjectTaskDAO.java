package com.awesome.domains.ProjectTask.entities;

import com.awesome.domains.ProjectTask.entities.ProjectTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectTaskDAO extends JpaRepository<ProjectTaskEntity, Long> {
    List<ProjectTaskEntity> findAllByProjectId(Long projectId);
}
