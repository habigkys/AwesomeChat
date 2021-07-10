package com.awesome.domains.services;

import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectDAO;
import com.awesome.domains.entities.ProjectTask;
import com.awesome.domains.entities.ProjectTaskDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private ProjectDAO projectDAO;
    private ProjectTaskDAO projectTaskDAO;

    public ProjectService(ProjectDAO projectDAO, ProjectTaskDAO projectTaskDao) {
        this.projectDAO = projectDAO;
        this.projectTaskDAO = projectTaskDAO;
    }

    public List<Project> getProjectList(){
        List<Project> projectList = projectDAO.findAll();

        return projectList;
    }

    public List<ProjectTask> getProjectTaskList(Long id){
        List<ProjectTask> projectTaskList = projectTaskDAO.findAllByProjectId(id);

        return projectTaskList;
    }

    public Project getProject(Long id){
        return projectDAO.findById(id).get();
    }

    public Project updateProject(Project project){
        Optional<Project> byId = projectDAO.findById(project.getId());

        if(project.getEndDate().isAfter(project.getStartDate())){
            // todo
        }

        Project toUpdateOne = byId.get();
        toUpdateOne.setId(project.getId());
        toUpdateOne.setProjectName(project.getProjectName());
        toUpdateOne.setSummary(project.getSummary());
        toUpdateOne.setStartDate(project.getStartDate());
        toUpdateOne.setEndDate(project.getEndDate());
        toUpdateOne.setUpdatedAt(LocalDateTime.now());

        return projectDAO.save(toUpdateOne);
    }

    public void deleteProject(Long id){
        projectDAO.deleteById(id);
    }
}
