package com.awesome.domains.project.validator;

import com.awesome.domains.AwesomeValidator;
import com.awesome.domains.project.dtos.ProjectDTO;

public class AwesomeProjectHasInvalidDate implements AwesomeValidator<ProjectDTO> {

    @Override
    public boolean validate(ProjectDTO projectDto) {
        return projectDto.getEndDate().isAfter(projectDto.getStartDate());
    }

    public static AwesomeProjectHasInvalidDate get() {
        return new AwesomeProjectHasInvalidDate();
    }
}
