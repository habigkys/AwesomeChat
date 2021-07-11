package com.awesome.controllers.api.v1;

import com.awesome.domains.services.ProjectTaskDTO;
import com.awesome.domains.services.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1/tasks")
public class ProjectTaskController {
    private final ProjectService projectService;

    public ProjectTaskController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * 1. 프로젝트 타스크/이슈 리스트
     * @return
     */
    @GetMapping("/")
    public List<ProjectTaskDTO> projectTaskList() {
        List<ProjectTaskDTO> projectTaskList = projectService.getProjectTaskList();

        return projectTaskList;
    }

    /**
     * 2. 특정 프로젝트 타스크/이슈
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ProjectTaskDTO projectTaskOne(@PathVariable("id") Long id) {
        ProjectTaskDTO projectTask = projectService.getProjectTask(id);

        return null;
    }

    /**
     * 3. 프로젝트 타스크/이슈 생성
     * @param projectTaskDto
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    public ProjectTaskDTO createProjectTask(@RequestBody ProjectTaskDTO projectTaskDto, @PathVariable("id") Long id) {
        ProjectTaskDTO projectTask = projectService.createProjectTask(projectTaskDto, id);

        return projectTask;
    }

    /**
     * 4. 프로젝트 타스크/이슈 업데이트
     * @param projectTaskDto
     * @return
     */
    @PutMapping("/{id}")
    public ProjectTaskDTO projectTaskUpdate(@RequestBody ProjectTaskDTO projectTaskDto) {
        ProjectTaskDTO updatedProjectTask = projectService.updateProjectTask(projectTaskDto);

        return updatedProjectTask;
    }

    /**
     * 5. 프로젝트 타스크/이슈 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public String projectTaskDelete(@PathVariable("id") Long id) {
        projectService.deleteProjectTask(id);

        return null;
    }
}