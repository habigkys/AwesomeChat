package com.awesome.domains.document.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentDAO extends JpaRepository<DocumentEntity, Long> {
}
