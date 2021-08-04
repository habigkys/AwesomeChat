package com.awesome.domains.document.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentDAO extends JpaRepository<DocumentEntity, Long> {
}
