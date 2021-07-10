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
    public String projectTaskList() {
        List<ProjectTask> projectTaskList = projectService.getProjectTaskList();

        return projectTaskList.toString();
    }

    @GetMapping("/{id}")
    public String projectTaskOne(@PathVariable("id") Long id) {
        ProjectTask projectTask = projectService.getProjectTask(id);

        return projectTask.toString();
    }

    @PostMapping("/{id}")
    public String projectList(@RequestBody ProjectTask projectTask, @PathVariable("id") Long id) {
        projectService.createProjectTask(projectTask, id);

        return "redirect:/";
    }

    @PutMapping("/{id}")
    public String projectTaskUpdate(@RequestBody ProjectTask projectTask) {
        ProjectTask updatedProjectTask = projectService.updateProjectTask(projectTask);

        return updatedProjectTask.toString();
    }

    @PutMapping("/{id}/Issue")
    public String projectTaskIssueUpdate(@RequestBody ProjectTask projectTask) {
        ProjectTask updatedProjectTask = projectService.updateProjectTaskIssue(projectTask);

        return updatedProjectTask.toString();
    }

    @DeleteMapping("/{id}")
    public String projectDelete(@PathVariable("id") Long id) {
        projectService.deleteProject(id);

        return "redirect:/";
    }
}