package com.awesome.domains.Project;

import com.awesome.domains.Project.entities.ProjectEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
// Project의 어그리게이트
public class Project {
    private List<ProjectEntity> projectEntities;

    public static Project create(){
        return new Project();
    }
}
