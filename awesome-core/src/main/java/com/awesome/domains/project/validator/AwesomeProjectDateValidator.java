package com.awesome.domains.project.validator;

import com.awesome.domains.AwesomeValidator;
import com.awesome.domains.project.dtos.ProjectDTO;

public class AwesomeProjectDateValidator implements AwesomeValidator<ProjectDTO> {

    @Override
    public boolean validate(ProjectDTO projectDto) {
        return projectDto.getEndDate().isAfter(projectDto.getStartDate());
    }

    public static AwesomeProjectDateValidator get() {
        return new AwesomeProjectDateValidator();
    }
}
