package com.awesome.domains.services;

import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectDAO;
import com.awesome.domains.entities.ProjectTask;
import com.awesome.domains.entities.ProjectTaskDAO;
import com.awesome.domains.enums.TaskType;
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

    public List<ProjectTask> getProjectTaskList(){
        List<ProjectTask> projectTaskList = projectTaskDAO.findAll();

        return projectTaskList;
    }

    public List<ProjectTask> getProjectTaskListByProject(Long id){
        List<ProjectTask> projectTaskList = projectTaskDAO.findAllByProjectId(id);

        return projectTaskList;
    }

    public Project getProject(Long id){
        return projectDAO.findById(id).get();
    }

    public ProjectTask getProjectTask(Long id){
        return projectTaskDAO.findById(id).get();
    }

    public List<Project> getProjectNameLike(String projectName){
        return projectDAO.findAllByProjectNameLike(projectName);
    }

    public Project updateProject(Project project){
        Optional<Project> byId = projectDAO.findById(project.getId());

        if(project.getEndDate().isAfter(project.getStartDate())){
            // todo
        }

        Project toUpdateOne = byId.get();
        toUpdateOne.setProjectName(project.getProjectName());
        toUpdateOne.setSummary(project.getSummary());
        toUpdateOne.setStartDate(project.getStartDate());
        toUpdateOne.setEndDate(project.getEndDate());
        toUpdateOne.setUpdatedAt(LocalDateTime.now());

        return projectDAO.save(toUpdateOne);
    }

    public ProjectTask updateProjectTask(ProjectTask projectTask){
        Optional<ProjectTask> byId = projectTaskDAO.findById(projectTask.getId());

        if(projectTask.getTaskEndDate().isAfter(projectTask.getTaskStartDate())){
            // todo
        }

        ProjectTask toUpdateOne = byId.get();
        toUpdateOne.setSummary(projectTask.getSummary());
        toUpdateOne.setPersons(projectTask.getPersons());
        toUpdateOne.setTaskStartDate(projectTask.getTaskStartDate());
        toUpdateOne.setTaskEndDate(projectTask.getTaskEndDate());
        toUpdateOne.setType(TaskType.TASK);
        toUpdateOne.setUpdatedAt(LocalDateTime.now());

        return projectTaskDAO.save(toUpdateOne);
    }

    public ProjectTask updateProjectTaskIssue(ProjectTask projectTask){
        Optional<ProjectTask> byId = projectTaskDAO.findById(projectTask.getId());

        if(projectTask.getTaskEndDate().isAfter(projectTask.getTaskStartDate())){
            // todo
        }

        ProjectTask toUpdateOne = byId.get();
        toUpdateOne.setSummary(projectTask.getSummary());
        toUpdateOne.setPersons(projectTask.getPersons());
        toUpdateOne.setTaskStartDate(projectTask.getTaskStartDate());
        toUpdateOne.setTaskEndDate(projectTask.getTaskEndDate());
        toUpdateOne.setType(TaskType.ISSUE);
        toUpdateOne.setUpdatedAt(LocalDateTime.now());

        return projectTaskDAO.save(toUpdateOne);
    }

    public void deleteProject(Long id){
        projectDAO.deleteById(id);
    }

    public Project createProject(Project project){
        if(project.getEndDate().isAfter(project.getStartDate())){
            // todo
        }

        Project toCreateProject = new Project();
        project.setProjectName(project.getProjectName());
        project.setSummary(project.getSummary());
        project.setStartDate(project.getStartDate());
        project.setEndDate(project.getEndDate());
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        return projectDAO.save(toCreateProject);
    }

    public ProjectTask createProjectTask(ProjectTask projectTask, Long id){
        if(projectTask.getTaskEndDate().isAfter(projectTask.getTaskStartDate())){
            // todo
        }

        ProjectTask toCreateProjectTask = new ProjectTask();
        toCreateProjectTask.setId(projectTask.getId());
        toCreateProjectTask.setProjectId(id);
        toCreateProjectTask.setPersons(projectTask.getPersons());
        toCreateProjectTask.setType(TaskType.TASK);
        toCreateProjectTask.setSummary(projectTask.getSummary());
        toCreateProjectTask.setTaskStartDate(projectTask.getTaskStartDate());
        toCreateProjectTask.setTaskEndDate(projectTask.getTaskEndDate());
        toCreateProjectTask.setCreatedAt(LocalDateTime.now());
        toCreateProjectTask.setUpdatedAt(LocalDateTime.now());

        return projectTaskDAO.save(toCreateProjectTask);
    }
}
