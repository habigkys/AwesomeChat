package com.awesome.controllers.api.v1;

import com.awesome.applications.tx.ProjectTXService;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.services.ProjectService;
import com.awesome.domains.user.dtos.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectTXService projectTXService;

    /**
     * 1. 프로젝트 리스트
     * @return
     */
    @GetMapping("/")
    public List<ProjectDTO> projectList() {
        List<ProjectDTO> projectList = projectService.getProjectList();

        return projectList;
    }

    /**
     * 2. 특정 프로젝트
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ProjectDTO projectOne(@PathVariable("id") Long id) {
        ProjectDTO project = projectService.getProject(id);

        return project;
    }

    /**
     * 3. 프로젝트 Like 검색
     * @param projectName
     * @return
     */
    @GetMapping("/nameLike/{name}")
    public List<ProjectDTO> projectNameLike(@PathVariable("name") String projectName) {
        List<ProjectDTO> projectNameLike = projectService.getProjectNameLike(projectName);

        return projectNameLike;
    }

    /**
     * 4. 프로젝트 생성
     * @param projectDto
     * @return
     */
    @PostMapping
    public ProjectDTO projectCreate(@RequestBody ProjectDTO projectDto, @RequestBody List<UserDTO> userDto) {
        ProjectDTO createdProject = projectTXService.createProject(projectDto, userDto);

        return createdProject;
    }

    /**
     * 5. 프로젝트 수정
     * @param projectDto
     * @return
     */
    @PutMapping("/{id}")
    public ProjectDTO projectUpdate(@RequestBody ProjectDTO projectDto, @RequestBody List<UserDTO> userDto) {
        ProjectDTO updatedProject = projectTXService.updateProject(projectDto, userDto);

        return updatedProject;
    }

    /**
     * 6. 프로젝트 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public String projectDelete(@PathVariable("id") Long id) {
        projectService.deleteProject(id);

        return null;
    }
}