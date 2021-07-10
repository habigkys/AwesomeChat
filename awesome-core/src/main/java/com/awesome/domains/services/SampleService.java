package com.awesome.domains.services;

import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SampleService {
    private ProjectDAO projectDAO;

    public SampleService(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Transactional
    public Long saveProject(Project project){
        return projectDAO.save(project).getId();
    }

    public List<Project> getProjectList(){
        List<Project> projectList = projectDAO.findAll();

        return projectList;
    }

    @Transactional
    public void updateProject(List<Project> projectList){
        for(Project projectToUpdateOne : projectList){
            Optional<Project> byId = projectDAO.findById(projectToUpdateOne.getId()); // 가능?

            Project toBeUpdate = byId.get();
            projectDAO.save(toBeUpdate);
        }
    }

    @Transactional
    public void deleteProject(List<Project> projectList){
        for(Project projectToDelOne : projectList){
            Optional<Project> byId = projectDAO.findById(projectToDelOne.getId()); // 가능?

            Project toBeDelete = byId.get();
            projectDAO.delete(toBeDelete);
        }
    }
}
