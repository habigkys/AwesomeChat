package com.awesome.domains.project.validator;

import com.awesome.domains.AwesomeBiValidator;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.enums.ProjectPriority;
import com.awesome.domains.project.enums.ProjectStatus;
import com.awesome.infrastructures.AwesomeException;
import com.awesome.infrastructures.AwesomeExceptionType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AwesomeProjectHasInvalidPriority implements AwesomeBiValidator<ProjectDTO, ProjectEntity> {
    @Override
    public boolean validate(ProjectDTO toUpdateProjectDto, ProjectEntity resentProjectEntity) {
        if(ProjectPriority.VERYHIGH.equals(toUpdateProjectDto.getProjectPriority())
                && ChronoUnit.WEEKS.between(resentProjectEntity.getEndDate(), LocalDateTime.now()) < 1){
            return true;
        }

        if(ProjectPriority.HIGH.equals(toUpdateProjectDto.getProjectPriority())
                && ChronoUnit.WEEKS.between(resentProjectEntity.getEndDate(), LocalDateTime.now()) < 2){
            return true;
        }

        return false;
    }

    public static AwesomeProjectHasInvalidPriority get() {
        return new AwesomeProjectHasInvalidPriority();
    }
}
