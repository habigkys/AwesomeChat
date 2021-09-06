package com.awesome.applications.service;

import com.awesome.applications.tx.ProjectTaskUserTXService;
import com.awesome.domains.mapping.entities.ProjectTaskUserDAO;
import com.awesome.domains.mapping.entities.ProjectTaskUserEntity;
import com.awesome.domains.projecttask.dtos.ProjectTaskDTO;
import com.awesome.domains.projecttask.entities.ProjectTaskDAO;
import com.awesome.domains.projecttask.entities.ProjectTaskEntity;
import com.awesome.domains.projecttask.validator.AwesomeProjectHasInvalidScopeUsers;
import com.awesome.domains.projecttask.validator.AwesomeProjectTaskHasInvalidDate;
import com.awesome.domains.projecttask.validator.AwesomeProjectTaskHasInvalidParent;
import com.awesome.domains.user.entities.UserDAO;
import com.awesome.infrastructures.exceptions.AwesomeException;
import com.awesome.infrastructures.exceptions.AwesomeExceptionType;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectTaskUserService {
    private ProjectTaskDAO projectTaskDao;
    private UserDAO userDao;
    private ProjectTaskUserDAO projectTaskUserDao;
    private ProjectTaskUserTXService projectTaskUserTXService;

    /**
     * 4. 프로젝트 타스크/이슈 생성 - ProjectTaskController
     * @param projectTaskDto
     * @param userIds
     * @return
     */
    @Transactional
    public ProjectTaskDTO createTask(ProjectTaskDTO projectTaskDto, List<Long> userIds){
        if(AwesomeProjectTaskHasInvalidDate.get().validate(projectTaskDto)) {
            throw new AwesomeException(AwesomeExceptionType.WRONG_DATE);
        }

        // 타스크의 스코프가 타스크일 경우 타스크 참여인력이 없으면 안됨. 스코프가 단순 이슈일 경우 참여인력이 필요없음
        if(AwesomeProjectHasInvalidScopeUsers.get().validate(projectTaskDto, userIds)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_TASK_USER);
        }

        // 타스크의 스코프가 타스크일 경우 Parent 프로젝트 또는 Parent 타스크가 지정되어야 함
        if(AwesomeProjectTaskHasInvalidParent.get().validate(projectTaskDto)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_TASK_PARENT);
        }

        return ProjectTaskDTO.convertEntityToDto(projectTaskUserTXService.save(ProjectTaskDTO.convertDtoToEntity(projectTaskDto), userIds));
    }

    /**
     * 5. 프로젝트 타스크/이슈 수정 - ProjectTaskController
     * @param projectTaskDto
     * @param userIds
     * @return
     */
    @Transactional
    public ProjectTaskDTO updateProjectTask(ProjectTaskDTO projectTaskDto, List<Long> userIds){
        if(AwesomeProjectTaskHasInvalidDate.get().validate(projectTaskDto)) {
            throw new AwesomeException(AwesomeExceptionType.WRONG_DATE);
        }

        Optional<ProjectTaskEntity> byId = projectTaskDao.findById(projectTaskDto.getId());

        if(byId.isEmpty()){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_TASK);
        }

        return ProjectTaskDTO.convertEntityToDto(projectTaskUserTXService.update(ProjectTaskDTO.convertDtoToEntity(projectTaskDto), userIds));
    }

    /**
     * 특정 타스크의 유저 ID 리스트 조회 - UserController
     * @param taskId
     * @return
     */
    public List<Long> getProjectTaskUserIdList(Long taskId) {
        List<ProjectTaskUserEntity> byTaskId = projectTaskUserDao.findAllByTaskId(taskId);

        if(CollectionUtils.isEmpty(byTaskId)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_TASK);
        }

        return byTaskId.stream().map(ProjectTaskUserEntity::getUserId).collect(Collectors.toList());
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
