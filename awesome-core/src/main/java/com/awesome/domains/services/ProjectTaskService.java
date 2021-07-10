package com.awesome.domains.services;

import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectDAO;
import com.awesome.domains.entities.ProjectTaskDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ProjectTaskService {
    private ProjectTaskDAO projectTaskDAO;

    public ProjectTaskService(ProjectTaskDAO projectTaskDAO) {
        this.projectTaskDAO = projectTaskDAO;
    }

    @Transactional
    public void updateProject(List<Project> projectList){
        projectTaskDAO.saveAll(projectList);
    }

    /** Project에 대한 서비스 정의
     *  Project 기간 설정
     *  Project 시작 종료 시간 변경
     *  Project Task
     */
}
