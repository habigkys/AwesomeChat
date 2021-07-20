package com.awesome.domains.mapping.dtos;

import com.awesome.domains.mapping.entities.ProjectUserEntity;
import com.awesome.domains.user.entities.UserEntity;
import com.awesome.domains.user.enums.UserPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProjectUserDTO {
    private Long id;

    private Long projectId;

    private String projectName;

    private Long userId;

    private String userName;

    private UserPosition userPosition;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ProjectUserDTO convert(ProjectUserEntity projectUserEntity) {
        ProjectUserDTO projectUserDTO = new ProjectUserDTO();
        projectUserDTO.setId(projectUserEntity.getId());
        projectUserDTO.setProjectId(projectUserDTO.getProjectId());
        projectUserDTO.setProjectName(projectUserDTO.getProjectName());
        projectUserDTO.setUserId(projectUserDTO.getUserId());
        projectUserDTO.setUserName(projectUserEntity.getUserName());
        projectUserDTO.setUserPosition(projectUserEntity.getUserPosition());
        projectUserDTO.setCreatedAt(projectUserEntity.getCreatedAt());
        projectUserDTO.setUpdatedAt(projectUserEntity.getUpdatedAt());
        return projectUserDTO;
    }
}
