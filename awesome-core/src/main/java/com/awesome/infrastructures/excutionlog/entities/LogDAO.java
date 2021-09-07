package com.awesome.infrastructures.excutionlog.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogDAO extends JpaRepository<LogEntity, Long> {
}
