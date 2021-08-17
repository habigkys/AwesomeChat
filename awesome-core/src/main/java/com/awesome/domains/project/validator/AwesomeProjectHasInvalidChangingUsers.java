package com.awesome.domains.project.validator;

import com.awesome.domains.AwesomeValidator;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.enums.ProjectPriority;

public class AwesomeProjectHasInvalidChangingUsers implements AwesomeValidator<ProjectDTO> {
    @Override
    public boolean validate(ProjectDTO projectDTO) {
        return (ProjectPriority.HIGH.equals(projectDTO.getProjectPriority()) || ProjectPriority.VERYHIGH.equals(projectDTO.getProjectPriority()));
    }

    public static AwesomeProjectHasInvalidChangingUsers get() {
        return new AwesomeProjectHasInvalidChangingUsers();
    }
}
