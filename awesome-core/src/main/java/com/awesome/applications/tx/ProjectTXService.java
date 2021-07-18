package com.awesome.applications.tx;

import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.entities.ProjectDAO;
import com.awesome.domains.project.enums.ProjectStatus;
import com.awesome.domains.projecttask.entities.ProjectTaskEntity;
import com.awesome.domains.projecttask.entities.ProjectTaskDAO;
import com.awesome.domains.user.dtos.UserDTO;
import com.awesome.domains.user.entities.UserDAO;
import com.awesome.domains.user.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectTXService {
    private ProjectDAO projectDao;
    private ProjectTaskDAO projectTaskDao;
    private UserDAO userDao;

    /**
     * 1. 프로젝트 생성 - ProjectController
     * @param projectDto
     * @return
     */
    public ProjectDTO createProject(ProjectDTO projectDto, List<UserDTO> userDto){
        if(!validateProjectDate(projectDto)) {
            throw new IllegalArgumentException();
        }

        // 프로젝트 참여인력이 없으면 안됨
        if(CollectionUtils.isEmpty(userDto)){
            throw new IllegalArgumentException();
        }

        // 과거 또는 현재의 프로젝트 생성시 TODO 불가
        if((LocalDate.now().isAfter(projectDto.getEndDate()) || LocalDate.now().isAfter(projectDto.getStartDate()) && LocalDate.now().isBefore(projectDto.getEndDate()))
                && ProjectStatus.TODO.equals(projectDto.getStatus())){
            throw new IllegalArgumentException();
        }

        ProjectEntity toCreateProjectEntity = new ProjectEntity();
        List<UserEntity> toCreateProjectPersons = new ArrayList<>();

        for(UserDTO user : userDto){
            UserEntity userEntity = new UserEntity();
            userEntity.setId(user.getId());
            userEntity.setUserName(user.getUserName());
            userEntity.setUserYear(user.getUserYear());
            userEntity.setUserPosition(user.getUserPosition());
            userEntity.setCreatedAt(user.getCreatedAt());
            userEntity.setUpdatedAt(user.getUpdatedAt());

            toCreateProjectPersons.add(userEntity);
        }

        toCreateProjectEntity.setProjectName(projectDto.getProjectName());
        toCreateProjectEntity.setSummary(projectDto.getSummary());
        toCreateProjectEntity.setStatus(projectDto.getStatus());
        toCreateProjectEntity.setProjectPersons(toCreateProjectPersons);
        toCreateProjectEntity.setStartDate(projectDto.getStartDate());
        toCreateProjectEntity.setEndDate(projectDto.getEndDate());
        toCreateProjectEntity.setCreatedAt(LocalDateTime.now());
        toCreateProjectEntity.setUpdatedAt(LocalDateTime.now());

        return ProjectDTO.convert(projectDao.save(toCreateProjectEntity));
    }

    /**
     * 2. 프로젝트 수정 - ProjectController
     * @param projectDto
     * @return
     */
    public ProjectDTO updateProject(ProjectDTO projectDto, List<UserDTO> userDto){
        if(!validateProjectDate(projectDto)) {
            throw new IllegalArgumentException();
        }

        Optional<ProjectEntity> byId = projectDao.findById(projectDto.getId());

        ProjectEntity toUpdateOne = byId.get();

        // TODO 상태가 아닌 프로젝트의 상태를 CANCELED로 변경하려고 할 때 생성일부터 일주일 미만인 프로젝트일 때 변경 불가
        if(ProjectStatus.CANCELED.equals(projectDto.getStatus()) && !ProjectStatus.TODO.equals(toUpdateOne.getStatus())
                && ChronoUnit.WEEKS.between(toUpdateOne.getCreatedAt(), LocalDateTime.now()) < 1){
            throw new IllegalArgumentException();
        }

        // 프로젝트 참여인력 변경이 있을 경우
        if(!CollectionUtils.isEmpty(userDto)){
            List<UserEntity> toCreateProjectPersons = new ArrayList<>();

            for(UserDTO user : userDto){
                UserEntity userEntity = new UserEntity();
                userEntity.setId(user.getId());
                userEntity.setUserName(user.getUserName());
                userEntity.setUserYear(user.getUserYear());
                userEntity.setUserPosition(user.getUserPosition());
                userEntity.setCreatedAt(user.getCreatedAt());
                userEntity.setUpdatedAt(user.getUpdatedAt());

                toCreateProjectPersons.add(userEntity);
            }

            toUpdateOne.setProjectPersons(toCreateProjectPersons);
        }

        toUpdateOne.setProjectName(projectDto.getProjectName());
        toUpdateOne.setSummary(projectDto.getSummary());
        toUpdateOne.setStatus(projectDto.getStatus());
        toUpdateOne.setStartDate(projectDto.getStartDate());
        toUpdateOne.setEndDate(projectDto.getEndDate());
        toUpdateOne.setUpdatedAt(LocalDateTime.now());

        return ProjectDTO.convert(projectDao.save(toUpdateOne));
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
