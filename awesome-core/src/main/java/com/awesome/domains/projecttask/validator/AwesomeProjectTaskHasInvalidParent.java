package com.awesome.domains.projecttask.validator;

import com.awesome.domains.AwesomeValidator;
import com.awesome.domains.projecttask.dtos.ProjectTaskDTO;
import com.awesome.domains.projecttask.enums.TaskType;

public class AwesomeProjectTaskHasInvalidParent implements AwesomeValidator<ProjectTaskDTO> {

    @Override
    public boolean validate(ProjectTaskDTO projectTaskDTO) {
        return TaskType.TASK.equals(projectTaskDTO.getType()) && (projectTaskDTO.getProjectId() == null || projectTaskDTO.getParentTaskId() == null);
    }

    public static AwesomeProjectTaskHasInvalidParent get() {
        return new AwesomeProjectTaskHasInvalidParent();
    }
}
