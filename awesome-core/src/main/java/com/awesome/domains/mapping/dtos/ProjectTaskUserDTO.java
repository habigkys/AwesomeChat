package com.awesome.domains.mapping.dtos;

import com.awesome.domains.mapping.entities.ProjectTaskUserEntity;
import com.awesome.domains.user.enums.UserPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProjectTaskUserDTO {
    private Long id;

    private Long taskId;

    private Long userId;

    private UserPosition userPosition;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ProjectTaskUserDTO convert(ProjectTaskUserEntity projectTaskUserEntity) {
        ProjectTaskUserDTO projectTaskUserDTO = new ProjectTaskUserDTO();
        projectTaskUserDTO.setId(projectTaskUserEntity.getId());
        projectTaskUserDTO.setTaskId(projectTaskUserEntity.getTaskId());
        projectTaskUserDTO.setUserId(projectTaskUserEntity.getUserId());
        projectTaskUserDTO.setUserPosition(projectTaskUserEntity.getUserPosition());
        projectTaskUserDTO.setCreatedAt(projectTaskUserEntity.getCreatedAt());
        projectTaskUserDTO.setUpdatedAt(projectTaskUserEntity.getUpdatedAt());
        return projectTaskUserDTO;
    }
}
