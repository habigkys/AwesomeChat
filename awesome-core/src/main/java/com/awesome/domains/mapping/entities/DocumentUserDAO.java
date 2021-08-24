package com.awesome.domains.mapping.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentUserDAO extends JpaRepository<DocumentUserEntity, Long> {
    List<DocumentUserEntity> findAllByDocumentId(Long documentId);
    List<DocumentUserEntity> findAllByUserId(Long userId);
    void deleteAllByDocumentId(Long documentId);
}
