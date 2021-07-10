package com.awesome.domains.services;

import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectDAO;
import com.awesome.domains.entities.ProjectTaskDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class ProjectService {
    private ProjectDAO projectDAO;

    public ProjectService(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    /** Project에 대한 서비스 정의
     *  Project 시작 종료 시간 변경 (end가 start보다 클수 없음)
     */

    @Transactional
    public void updateProjectDate(Project project){
        Optional<Project> byId = projectDAO.findById(project.getId());

        Project toDateUpdateOne = byId.get();
        toDateUpdateOne.setStartDate(project.getStartDate());
        toDateUpdateOne.setEndDate(project.getEndDate());

        projectDAO.save(toDateUpdateOne);
    }
}
