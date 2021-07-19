package com.awesome.domains.mapping.entities;

import com.awesome.domains.project.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectUserDAO extends JpaRepository<ProjectUserEntity, Long> {
    List<ProjectUserEntity> findAllByProjectId(Long projectId);
}
