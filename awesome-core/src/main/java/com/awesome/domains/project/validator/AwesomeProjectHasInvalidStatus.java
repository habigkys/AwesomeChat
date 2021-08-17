package com.awesome.domains.project.validator;

import com.awesome.domains.AwesomeBiValidator;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.enums.ProjectStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AwesomeProjectHasInvalidStatus implements AwesomeBiValidator<ProjectDTO, ProjectEntity> {
    @Override
    public boolean validate(ProjectDTO toUpdateProjectDto, ProjectEntity resentProjectEntity) {
        return (ProjectStatus.CANCELED.equals(toUpdateProjectDto.getStatus()) && !ProjectStatus.TODO.equals(resentProjectEntity.getStatus())
                && ChronoUnit.WEEKS.between(resentProjectEntity.getCreatedAt(), LocalDateTime.now()) < 1);
    }

    public static AwesomeProjectHasInvalidStatus get() {
        return new AwesomeProjectHasInvalidStatus();
    }
}
