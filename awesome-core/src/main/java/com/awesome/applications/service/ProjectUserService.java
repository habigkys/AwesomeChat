package com.awesome.applications.service;

import com.awesome.applications.tx.ProjectUserTXService;
import com.awesome.domains.mapping.entities.ProjectUserDAO;
import com.awesome.domains.mapping.entities.ProjectUserEntity;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectDAO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.validator.*;
import com.awesome.domains.user.dtos.UserDTO;
import com.awesome.domains.user.entities.UserDAO;
import com.awesome.domains.user.entities.UserEntity;
import com.awesome.domains.user.enums.UserPosition;
import com.awesome.infrastructures.exceptions.AwesomeException;
import com.awesome.infrastructures.exceptions.AwesomeExceptionType;
import com.awesome.infrastructures.excutionlog.LogExecutionTime;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectUserService {
    private ProjectDAO projectDao;
    private UserDAO userDao;
    private ProjectUserDAO projectUserDao;
    private ProjectUserTXService projectUserTXService;

    /**
     * 4. 프로젝트 생성 - ProjectController
     * @param projectDto
     * @param userIds
     * @return
     */
    @Transactional
    @LogExecutionTime
    public ProjectDTO createProject(ProjectDTO projectDto, List<Long> userIds){
        // 종료일이 시작일보다 먼저 올 수 없음
        if(AwesomeProjectHasInvalidDate.get().validate(projectDto)) {
            throw new AwesomeException(AwesomeExceptionType.WRONG_DATE);
        }

        // 프로젝트 참여인력이 없으면 안됨
        if(CollectionUtils.isEmpty(userIds)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_PROJECT_USER);
        }

        // 프로젝트 리더는 1명을 초과할 수 없음
        List<UserEntity> users = userDao.findAllById(userIds);
        List<UserEntity> leaders = users.stream().filter(e -> UserPosition.LEADER.equals(e.getUserPosition())).collect(Collectors.toList());
        if(leaders.size() > 1){
            throw new AwesomeException(AwesomeExceptionType.MULTI_LEADER);
        }

        // 과거 또는 현재의 프로젝트 생성시 TODO 불가
        if(AwesomeProjectHasInvalidTodoDate.get().validate(projectDto)){
            throw new AwesomeException(AwesomeExceptionType.TODO_DATE_INVALID);
        }

        // txService create + project&user mapping Atomic 동작 보장
        return ProjectDTO.convertEntityToDto(projectUserTXService.save(ProjectDTO.convertDtoToEntity(projectDto), userIds));
    }

    /**
     * 5. 프로젝트 수정 - ProjectController
     * @param projectDto
     * @param userIds
     * @return
     */
    @Transactional
    @LogExecutionTime
    public ProjectDTO updateProject(ProjectDTO projectDto, List<Long> userIds){
        // 종료일이 시작일보다 먼저 올 수 없음
        if(AwesomeProjectHasInvalidDate.get().validate(projectDto)) {
            throw new AwesomeException(AwesomeExceptionType.WRONG_DATE);
        }

        Optional<ProjectEntity> byId = projectDao.findById(projectDto.getId());

        if(byId.isEmpty()){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_PROJECT);
        }

        ProjectEntity toUpdateOne = byId.get();

        // TODO 상태가 아닌 프로젝트의 상태를 CANCELED로 변경하려고 할 때 생성일부터 일주일 미만인 프로젝트일 때 변경 불가
        if(AwesomeProjectHasInvalidStatus.get().validate(projectDto, toUpdateOne)){
            throw new AwesomeException(AwesomeExceptionType.ONE_WEEK_CANCEL);
        }

        // 프로젝트 종료까지 일주일 미만 남았으면 프로젝트 우선순위를 VERYHIGH로 변경 불가
        // 프로젝트 종료까지 이주일 미만 남았으면 프로젝트 우선순위를 HIGH로 변경 불가
        if(AwesomeProjectHasInvalidPriority.get().validate(projectDto, toUpdateOne)){
            throw new AwesomeException(AwesomeExceptionType.WRONG_PRIORITY);
        }

        // 프로젝트 참여인력 변경이 있을 경우
        if(!CollectionUtils.isEmpty(userIds)){
            // 프로젝트 우선순위가 VERYHIGH 또는 HIGH로 급할 경우 인력 교체 불가
            if(AwesomeProjectHasInvalidChangingUsers.get().validate(projectDto)){
                throw new AwesomeException(AwesomeExceptionType.HIGH_PRIORITY_USER_CHANGE);
            }

            // 프로젝트 리더는 1명을 초과할 수 없음
            List<UserEntity> users = userDao.findAllById(userIds);
            List<UserEntity> leaders = users.stream().filter(e -> UserPosition.LEADER.equals(e.getUserPosition())).collect(Collectors.toList());
            if(leaders.size() > 1){
                throw new AwesomeException(AwesomeExceptionType.MULTI_LEADER);
            }
        }

        // txService update + project&user mapping Atomic 동작 보장
        return ProjectDTO.convertEntityToDto(projectUserTXService.update(ProjectDTO.convertDtoToEntity(projectDto), userIds));
    }

    /**
     * 7. 특정 프로젝트의 유저 ID 리스트 조회 - UserController
     * @param projectId
     * @return
     */
    public List<Long> getProjectUserIdList(Long projectId){
        List<ProjectUserEntity> byProjectId = projectUserDao.findAllByProjectId(projectId);

        if(CollectionUtils.isEmpty(byProjectId)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_PROJECT);
        }

        return byProjectId.stream().map(ProjectUserEntity::getUserId).collect(Collectors.toList());
    }

    /**
     * 7. 특정 프로젝트의 유저 리스트 조회 - ProjectController
     * @param projectId
     * @return
     */
    public List<UserDTO> getProjectUserList(Long projectId){
        List<ProjectUserEntity> byProjectId = projectUserDao.findAllByProjectId(projectId);

        if(CollectionUtils.isEmpty(byProjectId)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_PROJECT);
        }

        List<Long> userIds = byProjectId.stream().map(ProjectUserEntity::getUserId).collect(Collectors.toList());

        return userDao.findAllById(userIds).stream().map(UserDTO::convertEntityToDto).collect(Collectors.toList());
    }

    /**
     * 8. 특정 유저의 프로젝트 리스트 조회 - UserController
     * @param userId
     * @return
     */
    public List<ProjectDTO> getUserProjectList(Long userId){
        List<ProjectUserEntity> byUserId = projectUserDao.findAllByUserId(userId);

        if(CollectionUtils.isEmpty(byUserId)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_USER);
        }

        List<Long> projectIds = byUserId.stream().map(ProjectUserEntity::getProjectId).collect(Collectors.toList());
        return projectDao.findAllById(projectIds).stream().map(ProjectDTO::convertEntityToDto).collect(Collectors.toList());
    }
}
