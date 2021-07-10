package com.awesome.applications.tx;

import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectDAO;
import com.awesome.domains.entities.ProjectTask;
import com.awesome.domains.entities.ProjectTaskDAO;
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

    public Long saveProject(Project project){
        return projectDAO.save(project).getId();
    }

    public List<Project> getProjectList(){
        List<Project> projectList = projectDAO.findAll();

        return projectList;
    }

    public List<ProjectTask> getProjectTaskList(){
        List<ProjectTask> projectTaskList = projectTaskDAO.findAll();

        return projectTaskList;
    }

    @Transactional
    public void updateProject(List<Project> projectList){
        projectDAO.saveAll(projectList);
    }

    @Transactional
    public void deleteProject(List<Project> projectList){
        projectDAO.deleteAll(projectList);
    }
}
