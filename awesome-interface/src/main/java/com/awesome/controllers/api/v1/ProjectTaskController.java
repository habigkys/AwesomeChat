package com.awesome.controllers.api.v1;

import com.awesome.domains.projecttask.dtos.ProjectTaskDTO;
import com.awesome.domains.projecttask.services.ProjectTaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/tasks")
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
     * @param taskId
     * @return
     */
    @GetMapping("/{taskId}")
    public ProjectTaskDTO projectTaskOne(@PathVariable("taskId") Long taskId) {
        ProjectTaskDTO projectTask = projectTaskService.getProjectTask(taskId);

        return null;
    }


    /**
     * 3. 특정 프로젝트의 타스크 리스트
     * @param projectId
     * @return
     */
    @GetMapping("/taskList/{projectId}")
    public List<ProjectTaskDTO> projectTaskList(@PathVariable("projectId") Long projectId) {
        List<ProjectTaskDTO> projectTaskList = projectTaskService.getProjectTaskListByProject(projectId);

        return projectTaskList;
    }

    /**
     * 4. 프로젝트 타스크/이슈 생성
     * @param projectTaskDto
     * @param projectId
     * @return
     */
    @PostMapping("/")
    public ProjectTaskDTO createProjectTask(@RequestBody ProjectTaskDTO projectTaskDto, @PathVariable("projectId") Long projectId) {
        ProjectTaskDTO projectTask = projectTaskService.createProjectTask(projectTaskDto, projectId);

        return projectTask;
    }

    /**
     * 5. 프로젝트 타스크/이슈 업데이트
     * @param projectTaskDto
     * @return
     */
    @PutMapping("/{taskId}")
    public ProjectTaskDTO projectTaskUpdate(@RequestBody ProjectTaskDTO projectTaskDto, @PathVariable("taskId") Long taskId) {
        ProjectTaskDTO updatedProjectTask = projectTaskService.updateProjectTask(projectTaskDto, taskId);

        return updatedProjectTask;
    }

    /**
     * 6. 프로젝트 타스크/이슈 삭제
     * @param taskId
     * @return
     */
    @DeleteMapping("/{taskId}")
    public String projectTaskDelete(@PathVariable("id") Long taskId) {
        projectTaskService.deleteProjectTask(taskId);

        return null;
    }
}