package com.awesome.domains.project.validator;

import com.awesome.domains.AwesomeValidator;
import com.awesome.domains.project.dtos.ProjectDTO;

public class AwesomeProjectHasInvalidDate implements AwesomeValidator<ProjectDTO> {

    @Override
    public boolean validate(ProjectDTO projectDto) {
        return projectDto.getStartDate().isAfter(projectDto.getEndDate());
    }

    public static AwesomeProjectHasInvalidDate get() {
        return new AwesomeProjectHasInvalidDate();
    }
}
