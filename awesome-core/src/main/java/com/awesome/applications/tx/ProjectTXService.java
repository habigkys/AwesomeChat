package com.awesome.applications.tx;

import com.awesome.domains.Project.entities.ProjectEntity;
import com.awesome.domains.Project.entities.ProjectDAO;
import com.awesome.domains.ProjectTask.entities.ProjectTaskEntity;
import com.awesome.domains.ProjectTask.entities.ProjectTaskDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectTXService {
    private ProjectDAO projectDAO;
    private ProjectTaskDAO projectTaskDAO;

    public ProjectTXService(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public Long saveProject(ProjectEntity projectEntity){
        return projectDAO.save(projectEntity).getId();
    }

    public List<ProjectEntity> getProjectList(){
        List<ProjectEntity> projectEntityList = projectDAO.findAll();

        return projectEntityList;
    }

    public List<ProjectTaskEntity> getProjectTaskList(){
        List<ProjectTaskEntity> projectTaskEntityList = projectTaskDAO.findAll();

        return projectTaskEntityList;
    }

    @Transactional
    public void updateProject(List<ProjectEntity> projectEntityList){
        projectDAO.saveAll(projectEntityList);
    }

    @Transactional
    public void deleteProject(List<ProjectEntity> projectEntityList){
        projectDAO.deleteAll(projectEntityList);
    }
}
