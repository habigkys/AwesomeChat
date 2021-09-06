package com.awesome.domains.project.services;

import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.entities.ProjectDAO;
import com.awesome.infrastructures.excutionlog.LogExecutionTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectService {
    private ProjectDAO projectDAO;

    /**
     * 1. 프로젝트 리스트 - ProjectController
     * @return
     */
    @LogExecutionTime
    public List<ProjectDTO> getProjectList(){
        List<ProjectEntity> projectEntityList = projectDAO.findAll();

        return projectEntityList.stream().map(ProjectDTO::convertEntityToDto).collect(Collectors.toList());
    }

    /**
     * 2. 특정 프로젝트 - ProjectController
     * @param projectId
     * @return
     */
    public ProjectDTO getProject(Long projectId){
        return ProjectDTO.convertEntityToDto(projectDAO.findById(projectId).get());
    }

    /**
     * 3. 프로젝트 Like 검색 - ProjectController
     * @param projectName
     * @return
     */
    public List<ProjectDTO> getProjectNameLike(String projectName){
        List<ProjectEntity> projectEntityNameLikeList = projectDAO.findAllByProjectNameLike(projectName);

        return projectEntityNameLikeList.stream().map(ProjectDTO::convertEntityToDto).collect(Collectors.toList());
    }

    /**
     * 3. 프로젝트 삭제 - ProjectController
     * @param projectId
     */
    public void deleteProject(Long projectId){
        projectDAO.deleteById(projectId);
    }
}
