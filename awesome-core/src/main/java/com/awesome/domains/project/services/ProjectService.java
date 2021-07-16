package com.awesome.domains.project.services;

import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.entities.ProjectDAO;
import com.awesome.domains.project.enums.ProjectStatus;
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
        if(!validateProjectDate(projectDto)) {
            throw new IllegalArgumentException();
        }

        // 현재날짜보다 과거에 있었던 프로젝트를 새로 생성하려고 할 때
        if(LocalDate.now().isAfter(projectDto.getEndDate())){
            // 과거의 프로젝트는 TODO 상태를 가질 수 없음
            if(projectDto.getStatus().equals(ProjectStatus.TODO)){
                throw new IllegalArgumentException();
            }
        // 프로젝트 시작~종료일 사이에 현재가 있을 때
        }else if(LocalDate.now().isAfter(projectDto.getStartDate()) && LocalDate.now().isBefore(projectDto.getEndDate())){
            // 현재의 프로젝트는 TODO 상태를 가질 수 없음
            if(projectDto.getStatus().equals(ProjectStatus.TODO)){
                throw new IllegalArgumentException();
            }
        }

        ProjectEntity toCreateProjectEntity = new ProjectEntity();
        toCreateProjectEntity.setProjectName(projectDto.getProjectName());
        toCreateProjectEntity.setSummary(projectDto.getSummary());
        toCreateProjectEntity.setStatus(projectDto.getStatus());
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
        if(!validateProjectDate(projectDto)) {
            throw new IllegalArgumentException();
        }

        Optional<ProjectEntity> byId = projectDAO.findById(projectDto.getId());

        ProjectEntity toUpdateOne = byId.get();

        // TODO 상태가 아닌 프로젝트의 상태를 CANCELED로 변경하려고 할 때
        if(projectDto.getStatus().equals(ProjectStatus.CANCELED) && !toUpdateOne.getStatus().equals(ProjectStatus.TODO)){
            // 프로젝트 생성일부터 현재까지 1주일 미만일 때 변경 불가
            if(ChronoUnit.WEEKS.between(toUpdateOne.getCreatedAt(), LocalDateTime.now()) < 1){
                // todo
                throw new IllegalArgumentException();
            }
        }

        toUpdateOne.setProjectName(projectDto.getProjectName());
        toUpdateOne.setSummary(projectDto.getSummary());
        toUpdateOne.setStatus(projectDto.getStatus());
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
    private boolean validateProjectDate(ProjectDTO projectDto) {
        return projectDto.getEndDate().isAfter(projectDto.getStartDate());
    }
}
