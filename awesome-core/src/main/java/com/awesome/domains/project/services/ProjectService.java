package com.awesome.domains.project.services;

import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.entities.ProjectDAO;
import com.awesome.domains.project.enums.ProjectStatus;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectService {
    private ProjectDAO projectDAO;

    /**
     * 1. 프로젝트 리스트 - ProjectController
     * @return
     */
    public List<ProjectDTO> getProjectList(){
        List<ProjectEntity> projectEntityList = projectDAO.findAll();

        return projectEntityList.stream().map(ProjectDTO::convert).collect(Collectors.toList());
    }

    /**
     * 2. 특정 프로젝트 - ProjectController
     * @param projectId
     * @return
     */
    public ProjectDTO getProject(Long projectId){
        return ProjectDTO.convert(projectDAO.findById(projectId).get());
    }

    /**
     * 3. 프로젝트 Like 검색 - ProjectController
     * @param projectName
     * @return
     */
    public List<ProjectDTO> getProjectNameLike(String projectName){
        List<ProjectEntity> projectEntityNameLikeList = projectDAO.findAllByProjectNameLike(projectName);

        return projectEntityNameLikeList.stream().map(ProjectDTO::convert).collect(Collectors.toList());
    }

    /**
     * 3. 프로젝트 삭제 - ProjectController
     * @param projectId
     */
    public void deleteProject(Long projectId){
        projectDAO.deleteById(projectId);
    }
}
