package com.awesome.domains.mapping.entities;

import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.user.enums.UserPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectUserDAO extends JpaRepository<ProjectUserEntity, Long> {
    List<ProjectUserEntity> findAllByProjectId(Long projectId);
    List<ProjectUserEntity> findAllByUserId(Long userId);
    List<ProjectUserEntity> findAllByProjectIdAndUserPosition(Long projectId, UserPosition userPosition);
}
