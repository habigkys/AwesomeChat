package com.awesome.domains.Project.services;

import com.awesome.domains.Project.entities.ProjectEntity;
import com.awesome.domains.Project.entities.ProjectDAO;
import com.awesome.domains.ProjectTask.entities.ProjectTaskEntity;
import com.awesome.domains.ProjectTask.entities.ProjectTaskDAO;
import com.awesome.domains.ProjectTask.services.ProjectTaskDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
     * @param id
     * @return
     */
    public ProjectDTO getProject(Long id){
        return ProjectDTO.convert(projectDAO.findById(id).get());
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
     * 4. 프로젝트 생성 - ProjectController
     * @param projectDto
     * @return
     */
    public ProjectDTO createProject(ProjectDTO projectDto){
        if(!isCorrectProjectDate(projectDto)) {
            // todo
        }

        ProjectEntity toCreateProjectEntity = new ProjectEntity();
        toCreateProjectEntity.setProjectName(projectDto.getProjectName());
        toCreateProjectEntity.setSummary(projectDto.getSummary());
        toCreateProjectEntity.setStartDate(projectDto.getStartDate());
        toCreateProjectEntity.setEndDate(projectDto.getEndDate());
        toCreateProjectEntity.setCreatedAt(LocalDateTime.now());
        toCreateProjectEntity.setUpdatedAt(LocalDateTime.now());

        return ProjectDTO.convert(projectDAO.save(toCreateProjectEntity));
    }

    /**
     * 5. 프로젝트 수정 - ProjectController
     * @param projectDto
     * @return
     */
    public ProjectDTO updateProject(ProjectDTO projectDto){
        if(!isCorrectProjectDate(projectDto)) {
            // todo
        }

        Optional<ProjectEntity> byId = projectDAO.findById(projectDto.getId());

        ProjectEntity toUpdateOne = byId.get();
        toUpdateOne.setProjectName(projectDto.getProjectName());
        toUpdateOne.setSummary(projectDto.getSummary());
        toUpdateOne.setStartDate(projectDto.getStartDate());
        toUpdateOne.setEndDate(projectDto.getEndDate());
        toUpdateOne.setUpdatedAt(LocalDateTime.now());

        return ProjectDTO.convert(projectDAO.save(toUpdateOne));
    }

    /**
     * 6. 프로젝트 삭제 - ProjectController
     * @param id
     */
    public void deleteProject(Long id){
        projectDAO.deleteById(id);
    }

    /**
     * 프로젝트 시작일, 종료일 날짜 체크
     * @param projectDto
     * @return
     */
    private boolean isCorrectProjectDate(ProjectDTO projectDto) {
        return projectDto.getEndDate().isAfter(projectDto.getStartDate());
    }
}
