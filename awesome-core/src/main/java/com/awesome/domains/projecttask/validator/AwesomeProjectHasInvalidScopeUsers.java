package com.awesome.domains.projecttask.validator;

import com.awesome.domains.AwesomeBiValidator;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.enums.ProjectPriority;
import com.awesome.domains.projecttask.dtos.ProjectTaskDTO;
import com.awesome.domains.projecttask.enums.TaskType;
import com.awesome.domains.user.dtos.UserDTO;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AwesomeProjectHasInvalidScopeUsers implements AwesomeBiValidator<ProjectTaskDTO, List<Long>> {
    @Override
    public boolean validate(ProjectTaskDTO projectTaskDTO, List<Long> users) {
        return TaskType.TASK.equals(projectTaskDTO.getType()) && CollectionUtils.isEmpty(users);
    }

    public static AwesomeProjectHasInvalidScopeUsers get() {
        return new AwesomeProjectHasInvalidScopeUsers();
    }
}
