package com.awesome.controllers;

import com.awesome.controllers.api.v1.ProjectApiController;
import com.awesome.domains.entities.Project;
import com.awesome.domains.entities.ProjectTask;
import com.awesome.domains.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
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

    @GetMapping("/nameLike/{name}")
    public String projectNameLike(@PathVariable("name") String projectName) {
        List<Project> projectNameLike = projectService.getProjectNameLike(projectName);

        return projectNameLike.toString();
    }

    @GetMapping("/{id}/tasks")
    public String projectTaskList(@PathVariable("id") Long id) {
        List<ProjectTask> projectTaskList = projectService.getProjectTaskListByProject(id);

        return projectTaskList.toString();
    }

    @PostMapping
    public String projectList(@RequestBody Project project) {
        projectService.createProject(project);

        return "redirect:/";
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