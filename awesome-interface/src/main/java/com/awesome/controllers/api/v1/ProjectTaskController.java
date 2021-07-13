package com.awesome.controllers.api.v1;

import com.awesome.domains.ProjectTask.services.ProjectTaskDTO;
import com.awesome.domains.Project.services.ProjectService;
import com.awesome.domains.ProjectTask.services.ProjectTaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController("/api/v1/tasks")
public class ProjectTaskController {
    private final ProjectTaskService projectTaskService;

    /**
     * 1. 프로젝트 타스크/이슈 리스트
     * @return
     */
    @GetMapping("/")
    public List<ProjectTaskDTO> projectTaskList() {
        List<ProjectTaskDTO> projectTaskList = projectTaskService.getProjectTaskList();

        return projectTaskList;
    }

    /**
     * 2. 특정 프로젝트 타스크/이슈
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ProjectTaskDTO projectTaskOne(@PathVariable("id") Long id) {
        ProjectTaskDTO projectTask = projectTaskService.getProjectTask(id);

        return null;
    }


    /**
     * 3. 특정 프로젝트의 타스크 리스트
     * @param projectId
     * @return
     */
    @GetMapping("/{projectId}/tasks")
    public List<ProjectTaskDTO> projectTaskList(@PathVariable("projectId") Long projectId) {
        List<ProjectTaskDTO> projectTaskList = projectTaskService.getProjectTaskListByProject(projectId);

        return projectTaskList;
    }

    /**
     * 4. 프로젝트 타스크/이슈 생성
     * @param projectTaskDto
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    public ProjectTaskDTO createProjectTask(@RequestBody ProjectTaskDTO projectTaskDto, @PathVariable("id") Long id) {
        ProjectTaskDTO projectTask = projectTaskService.createProjectTask(projectTaskDto, id);

        return projectTask;
    }

    /**
     * 5. 프로젝트 타스크/이슈 업데이트
     * @param projectTaskDto
     * @return
     */
    @PutMapping("/{id}")
    public ProjectTaskDTO projectTaskUpdate(@RequestBody ProjectTaskDTO projectTaskDto) {
        ProjectTaskDTO updatedProjectTask = projectTaskService.updateProjectTask(projectTaskDto);

        return updatedProjectTask;
    }

    /**
     * 6. 프로젝트 타스크/이슈 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public String projectTaskDelete(@PathVariable("id") Long id) {
        projectTaskService.deleteProjectTask(id);

        return null;
    }
}