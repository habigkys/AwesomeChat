package com.awesome.controllers.api.v1;

import com.awesome.applications.tx.ProjectTXService;
import com.awesome.domains.document.enums.DocumentType;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.services.ProjectService;
import com.awesome.domains.user.dtos.UserDTO;
import com.awesome.dtos.ProjectDetail;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectTXService projectTXService;

    /**
     * 1. 프로젝트 리스트
     * @return
     */
    @GetMapping("/")
    public List<ProjectDTO> projectList() {
        return projectService.getProjectList();
    }

    /**
     * 2. 특정 프로젝트
     * @param projectId
     * @return
     */
    @GetMapping("/{projectId}")
    public ProjectDetail projectOne(@PathVariable("projectId") Long projectId) {
        ProjectDTO project = projectService.getProject(projectId);
        List<UserDTO> users = projectTXService.getProjectUserList(projectId);

        ProjectDetail projectDetail = new ProjectDetail();
        projectDetail.setProjectId(projectId);
        projectDetail.setProjectName(project.getProjectName());
        projectDetail.setSummary(project.getSummary());
        projectDetail.setStatus(project.getStatus());
        projectDetail.setProjectPriority(project.getProjectPriority());
        projectDetail.setStartDate(project.getStartDate());
        projectDetail.setEndDate(project.getEndDate());
        projectDetail.setUsers(users);

        return projectDetail;
    }

    /**
     * 3. 프로젝트 Like 검색
     * @param projectName
     * @return
     */
    @GetMapping("/nameLike/{projectName}")
    public List<ProjectDTO> projectNameLike(@PathVariable("projectName") String projectName) {
        List<ProjectDTO> projectNameLike = projectService.getProjectNameLike(projectName);

        return projectNameLike;
    }

    /**
     * 4. 프로젝트 생성
     * @param projectDetail
     * @return
     */
    @PostMapping("/")
    public ProjectDetail projectCreate(ProjectDetail projectDetail) {
        ProjectDTO projectDTO = getProjectDTO(projectDetail);
        List<UserDTO> users = projectDetail.getUsers();

        ProjectDTO createdProject = projectTXService.createProject(projectDTO, users);

        ProjectDetail createProjectDetail = new ProjectDetail();
        createProjectDetail.setProjectId(createdProject.getId());
        createProjectDetail.setProjectName(createdProject.getProjectName());
        createProjectDetail.setSummary(createdProject.getSummary());
        createProjectDetail.setStatus(createdProject.getStatus());
        createProjectDetail.setProjectPriority(createdProject.getProjectPriority());
        createProjectDetail.setStartDate(createdProject.getStartDate());
        createProjectDetail.setEndDate(createdProject.getEndDate());
        createProjectDetail.setUsers(users);

        return createProjectDetail;
    }

    /**
     * 5. 프로젝트 수정
     * @param projectDetail
     * @return
     */
    @PutMapping("/")
    public ProjectDTO projectUpdate(ProjectDetail projectDetail) {
        ProjectDTO projectDTO = getProjectDTO(projectDetail);
        List<UserDTO> users = projectDetail.getUsers();

        ProjectDTO updatedProject = projectTXService.updateProject(projectDTO, users);

        return updatedProject;
    }

    /**
     * 6. 프로젝트 삭제
     * @param projectId
     * @return
     */
    @DeleteMapping("/{projectId}")
    public String projectDelete(@PathVariable("id") Long projectId) {
        projectService.deleteProject(projectId);

        return null;
    }

    /**
     * 7. 프로젝트 산출물 수정
     * @param projectDto
     * @param documentTypes
     * @return
     */
    @PutMapping("/documents")
    public String projectDocumentUpdate(@RequestBody ProjectDTO projectDto, @RequestBody List<DocumentType> documentTypes) {
        projectTXService.updateProjectDocuments(projectDto, documentTypes);

        return null;
    }

    private ProjectDTO getProjectDTO(ProjectDetail projectDetail) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectName(projectDetail.getProjectName());
        projectDTO.setSummary(projectDetail.getSummary());
        projectDTO.setStatus(projectDetail.getStatus());
        projectDTO.setProjectPriority(projectDetail.getProjectPriority());
        projectDTO.setStartDate(projectDetail.getStartDate());
        projectDTO.setEndDate(projectDetail.getEndDate());
        return projectDTO;
    }
}