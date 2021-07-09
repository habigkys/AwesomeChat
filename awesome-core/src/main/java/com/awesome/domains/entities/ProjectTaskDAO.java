package com.awesome.domains.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectTaskDAO extends JpaRepository<ProjectTask, Long> {
    List<ProjectTask> findAllByProjectId(Long projectId);
}
