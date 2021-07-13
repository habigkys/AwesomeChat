package com.awesome.domains.ProjectTask;

import com.awesome.domains.ProjectTask.entities.ProjectTaskEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
// ProjectTask의 어그리게이트
public class ProjectTask {
    private List<ProjectTaskEntity> projectTaskEntities;

    public static ProjectTask create(){
        return new ProjectTask();
    }
}
