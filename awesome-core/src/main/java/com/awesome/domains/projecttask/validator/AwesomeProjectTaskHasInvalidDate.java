package com.awesome.domains.projecttask.validator;

import com.awesome.domains.AwesomeValidator;
import com.awesome.domains.projecttask.dtos.ProjectTaskDTO;

public class AwesomeProjectTaskHasInvalidDate implements AwesomeValidator<ProjectTaskDTO> {

    @Override
    public boolean validate(ProjectTaskDTO projectTaskDTO) {
        return projectTaskDTO.getTaskEndDate().isAfter(projectTaskDTO.getTaskEndDate());
    }

    public static AwesomeProjectTaskHasInvalidDate get() {
        return new AwesomeProjectTaskHasInvalidDate();
    }
}
