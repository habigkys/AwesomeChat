package com.awesome.applications.service;

import com.awesome.domains.mapping.entities.ProjectTaskUserDAO;
import com.awesome.domains.mapping.entities.ProjectTaskUserEntity;
import com.awesome.domains.projecttask.dtos.ProjectTaskDTO;
import com.awesome.domains.projecttask.entities.ProjectTaskDAO;
import com.awesome.domains.projecttask.entities.ProjectTaskEntity;
import com.awesome.domains.projecttask.validator.AwesomeProjectHasInvalidScopeUsers;
import com.awesome.domains.projecttask.validator.AwesomeProjectTaskHasInvalidDate;
import com.awesome.domains.projecttask.validator.AwesomeProjectTaskHasInvalidParent;
import com.awesome.domains.user.dtos.UserDTO;
import com.awesome.domains.user.entities.UserDAO;
import com.awesome.domains.user.entities.UserEntity;
import com.awesome.infrastructures.AwesomeException;
import com.awesome.infrastructures.AwesomeExceptionType;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectTaskUserService {
    private ProjectTaskDAO projectTaskDao;
    private UserDAO userDao;
    private ProjectTaskUserDAO projectTaskUserDao;

    /**
     * 4. 프로젝트 타스크/이슈 생성 - ProjectTaskController
     * @param projectTaskDto
     * @param users
     * @return
     */
    @Transactional
    public ProjectTaskDTO createTask(ProjectTaskDTO projectTaskDto, List<UserDTO> users){
        if(AwesomeProjectTaskHasInvalidDate.get().validate(projectTaskDto)) {
            throw new AwesomeException(AwesomeExceptionType.WRONG_DATE);
        }

        // 타스크의 스코프가 타스크일 경우 타스크 참여인력이 없으면 안됨. 스코프가 단순 이슈일 경우 참여인력이 필요없음
        if(AwesomeProjectHasInvalidScopeUsers.get().validate(projectTaskDto, users)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_TASK_USER);
        }

        // 타스크의 스코프가 타스크일 경우 Parent 프로젝트 또는 Parent 타스크가 지정되어야 함
        if(AwesomeProjectTaskHasInvalidParent.get().validate(projectTaskDto)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_TASK_PARENT);
        }

        ProjectTaskEntity savedProjectTaskEntity = projectTaskDao.save(ProjectTaskDTO.convertDtoToEntity(projectTaskDto));

        // 타스크 <> 유저 매핑 정보 저장
        projectTaskUserMapping(savedProjectTaskEntity.getId(), users);

        return ProjectTaskDTO.convertEntityToDto(savedProjectTaskEntity);
    }

    /**
     * 5. 프로젝트 타스크/이슈 수정 - ProjectTaskController
     * @param projectTaskDto
     * @param users
     * @return
     */
    @Transactional
    public ProjectTaskDTO updateProjectTask(ProjectTaskDTO projectTaskDto, List<UserDTO> users){
        if(AwesomeProjectTaskHasInvalidDate.get().validate(projectTaskDto)) {
            throw new AwesomeException(AwesomeExceptionType.WRONG_DATE);
        }

        Optional<ProjectTaskEntity> byId = projectTaskDao.findById(projectTaskDto.getId());

        if(byId.isEmpty()){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_TASK);
        }

        ProjectTaskEntity toUpdateOne = byId.get();

        // 타스크 참여인력 변경이 있을 경우
        if(!CollectionUtils.isEmpty(users)){
            List<ProjectTaskUserEntity> byTaskId = projectTaskUserDao.findAllByTaskId(projectTaskDto.getId());

            // 기존 매핑 정보 삭제 후
            projectTaskUserDao.deleteAllByTaskId(projectTaskDto.getId());

            // 타스크 <> 유저 매핑 정보 저장
            projectTaskUserMapping(projectTaskDto.getId(), users);
        }

        return ProjectTaskDTO.convertEntityToDto(projectTaskDao.save(ProjectTaskDTO.convertDtoToEntity(projectTaskDto)));
    }

    /**
     * 타스크 <> 유저 매핑
     * @param projectTaskId
     * @param users
     */
    private void projectTaskUserMapping(Long projectTaskId, List<UserDTO> users) {
        List<ProjectTaskUserEntity> projectTaskUserEntities = Lists.newArrayList();

        List<UserEntity> userEntities = userDao.findAllById(users.stream().map(e -> e.getId()).collect(Collectors.toList()));
        Map<Long, UserEntity> recentUserMap = userEntities.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

        for(UserDTO user : users){
            ProjectTaskUserEntity projectTaskUserEntity = new ProjectTaskUserEntity();
            projectTaskUserEntity.setTaskId(projectTaskId);
            projectTaskUserEntity.setUserId(user.getId());

            UserEntity recentUserEntity = recentUserMap.get(user.getId());
            projectTaskUserEntity.setUserPosition(recentUserEntity.getUserPosition());

            projectTaskUserEntities.add(projectTaskUserEntity);
        }

        projectTaskUserDao.saveAll(projectTaskUserEntities);
    }

    /**
     * 특정 타스크의 유저 리스트 조회 - UserController
     * @param taskId
     * @return
     */
    public List<UserDTO> getProjectTaskUserIdList(Long taskId) {
        List<ProjectTaskUserEntity> byTaskId = projectTaskUserDao.findAllByTaskId(taskId);

        if(CollectionUtils.isEmpty(byTaskId)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_TASK);
        }

        List<Long> userIds = byTaskId.stream().map(ProjectTaskUserEntity::getUserId).collect(Collectors.toList());
        return userDao.findAllById(userIds).stream().map(UserDTO::convertEntityToDto).collect(Collectors.toList());
    }

    /**
     * 특정 유저의 타스크 리스트 조회 - UserController
     * @param userId
     * @return
     */
    public List<ProjectTaskDTO> getUserTaskList(Long userId){
        List<ProjectTaskUserEntity> byUserId = projectTaskUserDao.findAllByUserId(userId);

        if(CollectionUtils.isEmpty(byUserId)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_USER);
        }

        List<Long> taskIds = byUserId.stream().map(ProjectTaskUserEntity::getTaskId).collect(Collectors.toList());
        return projectTaskDao.findAllById(taskIds).stream().map(ProjectTaskDTO::convertEntityToDto).collect(Collectors.toList());
    }
}
