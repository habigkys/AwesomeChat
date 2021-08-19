package com.awesome.domains.project.validator;

import com.awesome.domains.AwesomeValidator;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.enums.ProjectStatus;

import java.time.LocalDate;

public class AwesomeProjectHasInvalidTodoDate implements AwesomeValidator<ProjectDTO> {
    @Override
    public boolean validate(ProjectDTO projectDto) {
        return (LocalDate.now().isAfter(projectDto.getEndDate())
                || (LocalDate.now().isAfter(projectDto.getStartDate()) && LocalDate.now().isBefore(projectDto.getEndDate())))
                && ProjectStatus.TODO.equals(projectDto.getStatus());
    }

    public static AwesomeProjectHasInvalidTodoDate get() {
        return new AwesomeProjectHasInvalidTodoDate();
    }
}
