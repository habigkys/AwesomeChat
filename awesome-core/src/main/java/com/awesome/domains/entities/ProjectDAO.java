package com.awesome.domains.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectDAO extends JpaRepository<Project, Long> {
  List<Project> findAllByProjectNameLike(String projectName);
}
