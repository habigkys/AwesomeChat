package com.awesome.domains.mapping.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectDocumentDAO extends JpaRepository<ProjectDocumentEntity, Long> {
    List<ProjectDocumentEntity> findAllByProjectId(Long projectId);
}
