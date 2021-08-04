package com.awesome.controllers.api.v1;

import com.awesome.applications.tx.ProjectTXService;
import com.awesome.controllers.api.v1.dtos.ProjectDetailDTO;
import com.awesome.domains.document.enums.DocumentType;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.services.ProjectService;
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
    public ProjectDTO projectOne(@PathVariable("projectId") Long projectId) {
        ProjectDTO project = projectService.getProject(projectId);

        return project;
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
     * @param projectDetailDTO
     * @return
     */
    @PostMapping("/create")
    public ProjectDTO projectCreate(ProjectDetailDTO projectDetailDTO) {
        ProjectDTO projectDto = new ProjectDTO();
        projectDto.setProjectName(projectDetailDTO.getProjectName());
        projectDto.setSummary(projectDetailDTO.getSummary());
        projectDto.setStatus(projectDetailDTO.getStatus());
        projectDto.setProjectPriority(projectDetailDTO.getProjectPriority());
        projectDto.setStartDate(projectDetailDTO.getStartDate());
        projectDto.setEndDate(projectDetailDTO.getEndDate());

        Long[] userIds = projectDetailDTO.getUserIds();

        ProjectDTO createdProject = projectTXService.createProject(projectDto, userIds);

        return createdProject;
    }

    /**
     * 5. 프로젝트 수정
     * @param projectDto
     * @param projectId
     * @param userIds
     * @return
     */
    @PutMapping("/update/{projectId}")
    public ProjectDTO projectUpdate(@RequestBody ProjectDTO projectDto, @PathVariable("projectId") Long projectId, @RequestBody Long[] userIds) {
        ProjectDTO updatedProject = projectTXService.updateProject(projectDto, projectId, userIds);

        return updatedProject;
    }

    /**
     * 6. 프로젝트 삭제
     * @param projectId
     * @return
     */
    @DeleteMapping("/delete/{projectId}")
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
}