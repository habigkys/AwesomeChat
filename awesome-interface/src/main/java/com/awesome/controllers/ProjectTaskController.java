package com.awesome.controllers;

import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectTask;
import com.awesome.domains.services.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1/tasks")
public class ProjectTaskController {
    private final ProjectService projectService;

    public ProjectTaskController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/")
    public String projectList() {
        List<Project> projectList = projectService.getProjectList();

        return projectList.toString();
    }

    @GetMapping("/{id}")
    public String projectOne(@PathVariable("id") Long id) {
        Project project = projectService.getProject(id);

        return project.toString();
    }

    @GetMapping("/{id}/tasks")
    public String projectTaskList(@PathVariable("id") Long id) {
        List<ProjectTask> projectTaskList = projectService.getProjectTaskList(id);

        return projectTaskList.toString();
    }

    @PutMapping("/{id}")
    public String projectUpdate(@RequestBody Project project) {
        Project updatedProject = projectService.updateProject(project);

        return updatedProject.toString();
    }

    @DeleteMapping("/{id}")
    public String projectDelete(@PathVariable("id") Long id) {
        projectService.deleteProject(id);

        return "redirect:/";
    }
}