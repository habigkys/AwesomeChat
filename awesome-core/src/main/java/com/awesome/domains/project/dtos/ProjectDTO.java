package com.awesome.domains.project.dtos;

import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.enums.ProjectPriority;
import com.awesome.domains.project.enums.ProjectStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDTO {
    private Long id;

    private String projectName;

    private String summary;

    private ProjectStatus status;

    private ProjectPriority projectPriority;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Data Transfer라는 기능 및 책임을 몰아주기 위해 여기에 convert를 다 몰아줌
    public static ProjectDTO convertEntityToDto(ProjectEntity projectEntity) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(projectEntity.getId());
        dto.setProjectName(projectEntity.getProjectName());
        dto.setSummary(projectEntity.getSummary());
        dto.setStatus(projectEntity.getStatus());
        dto.setProjectPriority(projectEntity.getProjectPriority());
        dto.setStartDate(projectEntity.getStartDate());
        dto.setEndDate(projectEntity.getEndDate());
        dto.setCreatedAt(projectEntity.getCreatedAt());
        dto.setUpdatedAt(projectEntity.getUpdatedAt());
        return dto;
    }

    public static ProjectEntity convertDtoToEntity(ProjectDTO projectDTO) {
        ProjectEntity entity = new ProjectEntity();
        entity.setId(projectDTO.getId());
        entity.setProjectName(projectDTO.getProjectName());
        entity.setSummary(projectDTO.getSummary());
        entity.setStatus(projectDTO.getStatus());
        entity.setProjectPriority(projectDTO.getProjectPriority());
        entity.setStartDate(projectDTO.getStartDate());
        entity.setEndDate(projectDTO.getEndDate());
        entity.setCreatedAt(projectDTO.getCreatedAt());
        entity.setUpdatedAt(projectDTO.getUpdatedAt());
        return entity;
    }
}
