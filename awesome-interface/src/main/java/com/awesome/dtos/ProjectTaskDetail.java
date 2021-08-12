package com.awesome.dtos;

import com.awesome.domains.project.enums.ProjectPriority;
import com.awesome.domains.project.enums.ProjectStatus;
import com.awesome.domains.projecttask.enums.TaskPriority;
import com.awesome.domains.projecttask.enums.TaskType;
import com.awesome.domains.user.dtos.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectTaskDetail {
    private Long projectTaskId;

    private Long projectId;

    private Long parentTaskId;

    private String projectTaskName;

    private String taskSummary;

    private TaskType taskType;

    private TaskPriority taskPriority;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate taskStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate taskEndDate;

    List<UserDTO> users;
}
