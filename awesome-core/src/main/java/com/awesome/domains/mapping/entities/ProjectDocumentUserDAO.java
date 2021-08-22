package com.awesome.domains.mapping.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectDocumentUserDAO extends JpaRepository<ProjectDocumentUserEntity, Long> {
    List<ProjectDocumentUserEntity> findAllByProjectId(Long projectId);
    List<ProjectDocumentUserEntity> findAllByUserId(Long userId);
    List<ProjectDocumentUserEntity> findAllByDocumentId(Long documentId);
    void deleteAllByProjectId(Long projectId);
}
