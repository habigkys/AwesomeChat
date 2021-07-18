package com.awesome.domains.project.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectDAO extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findAllByProjectNameLike(String projectName);
}
