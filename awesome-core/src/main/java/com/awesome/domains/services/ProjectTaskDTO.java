package com.awesome.domains.services;

import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectTask;
import com.awesome.domains.enums.TaskType;
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

    private Long projectId;

    private Long parentTaskId;

    private String summary;

    private LocalDate taskStartDate;

    private LocalDate taskEndDate;

    private String persons;

    private TaskType type;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ProjectTaskDTO convert(ProjectTask projectTask) {
        ProjectTaskDTO taskDto = new ProjectTaskDTO();
        taskDto.setId(projectTask.getId());
        taskDto.setProjectId(projectTask.getProjectId());
        taskDto.setParentTaskId(projectTask.getParentTaskId());
        taskDto.setSummary(projectTask.getSummary());
        taskDto.setTaskStartDate(projectTask.getTaskStartDate());
        taskDto.setTaskEndDate(projectTask.getTaskEndDate());
        taskDto.setPersons(projectTask.getPersons());
        taskDto.setType(projectTask.getType());
        taskDto.setCreatedAt(projectTask.getCreatedAt());
        taskDto.setUpdatedAt(projectTask.getUpdatedAt());
        return taskDto;
    }
}
