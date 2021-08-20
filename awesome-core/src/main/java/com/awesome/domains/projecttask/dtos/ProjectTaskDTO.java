package com.awesome.domains.projecttask.dtos;

import com.awesome.domains.projecttask.entities.ProjectTaskEntity;
import com.awesome.domains.projecttask.enums.TaskPriority;
import com.awesome.domains.projecttask.enums.TaskType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProjectTaskDTO {
    private Long id;

    private String projectTaskName;

    private Long projectId;

    private Long parentTaskId;

    private String summary;

    private LocalDate taskStartDate;

    private LocalDate taskEndDate;

    private TaskType type;

    private TaskPriority taskPriority;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ProjectTaskDTO convertEntityToDto(ProjectTaskEntity projectTaskEntity) {
        ProjectTaskDTO taskDto = new ProjectTaskDTO();
        taskDto.setId(projectTaskEntity.getId());
        taskDto.setProjectTaskName(projectTaskEntity.getProjectTaskName());
        taskDto.setProjectId(projectTaskEntity.getProjectId());
        taskDto.setParentTaskId(projectTaskEntity.getParentTaskId());
        taskDto.setSummary(projectTaskEntity.getSummary());
        taskDto.setTaskStartDate(projectTaskEntity.getTaskStartDate());
        taskDto.setTaskEndDate(projectTaskEntity.getTaskEndDate());
        taskDto.setType(projectTaskEntity.getType());
        taskDto.setTaskPriority(projectTaskEntity.getTaskPriority());
        taskDto.setCreatedAt(projectTaskEntity.getCreatedAt());
        taskDto.setUpdatedAt(projectTaskEntity.getUpdatedAt());
        return taskDto;
    }

    public static ProjectTaskEntity convertDtoToEntity(ProjectTaskDTO projectTaskDTO) {
        ProjectTaskEntity entity = new ProjectTaskEntity();
        entity.setId(projectTaskDTO.getId());
        entity.setProjectTaskName(projectTaskDTO.getProjectTaskName());
        entity.setProjectId(projectTaskDTO.getProjectId());
        entity.setParentTaskId(projectTaskDTO.getParentTaskId());
        entity.setSummary(projectTaskDTO.getSummary());
        entity.setTaskStartDate(projectTaskDTO.getTaskStartDate());
        entity.setTaskEndDate(projectTaskDTO.getTaskEndDate());
        entity.setType(projectTaskDTO.getType());
        entity.setTaskPriority(projectTaskDTO.getTaskPriority());
        entity.setCreatedAt(projectTaskDTO.getCreatedAt());
        entity.setUpdatedAt(projectTaskDTO.getUpdatedAt());
        return entity;
    }
}
