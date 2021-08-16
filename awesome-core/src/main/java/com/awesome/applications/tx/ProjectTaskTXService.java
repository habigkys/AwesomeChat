package com.awesome.applications.tx;

import com.awesome.domains.mapping.entities.ProjectTaskUserDAO;
import com.awesome.domains.mapping.entities.ProjectTaskUserEntity;
import com.awesome.domains.mapping.entities.ProjectUserEntity;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.enums.ProjectPriority;
import com.awesome.domains.project.enums.ProjectStatus;
import com.awesome.domains.projecttask.dtos.ProjectTaskDTO;
import com.awesome.domains.projecttask.entities.ProjectTaskDAO;
import com.awesome.domains.projecttask.entities.ProjectTaskEntity;
import com.awesome.domains.projecttask.enums.TaskType;
import com.awesome.domains.user.dtos.UserDTO;
import com.awesome.domains.user.entities.UserDAO;
import com.awesome.domains.user.entities.UserEntity;
import com.awesome.infrastructures.AwesomeException;
import com.awesome.infrastructures.AwesomeExceptionType;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectTaskTXService {
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
        if(!validateProjectTaskDate(projectTaskDto)) {
            throw new AwesomeException(AwesomeExceptionType.WRONG_DATE);
        }

        // 타스크의 스코프가 타스크일 경우 타스크 참여인력이 없으면 안됨. 스코프가 단순 이슈일 경우 참여인력이 필요없음
        if(TaskType.TASK.equals(projectTaskDto.getType()) && CollectionUtils.isEmpty(users)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_TASK_USER);
        }

        // 타스크의 스코프가 타스크일 경우 Parent 프로젝트 또는 Parent 타스크가 지정되어야 함
        if(TaskType.TASK.equals(projectTaskDto.getType()) && (projectTaskDto.getProjectId() == null || projectTaskDto.getParentTaskId() == null)){
            Optional<Long> targetProjectId = Optional.ofNullable(projectTaskDto.getProjectId());
            Optional<Long> targetParentTaskId = Optional.ofNullable(projectTaskDto.getParentTaskId());

            if(targetProjectId.isEmpty() || targetParentTaskId.isEmpty()){
                throw new AwesomeException(AwesomeExceptionType.EMPTY_TASK_PARENT);
            }
        }

        ProjectTaskEntity toCreateProjectTaskEntity = new ProjectTaskEntity();

        toCreateProjectTaskEntity.setProjectId(projectTaskDto.getProjectId());
        toCreateProjectTaskEntity.setProjectTaskName(projectTaskDto.getProjectTaskName());
        toCreateProjectTaskEntity.setParentTaskId(projectTaskDto.getParentTaskId());
        toCreateProjectTaskEntity.setTaskPriority(projectTaskDto.getTaskPriority());
        toCreateProjectTaskEntity.setSummary(projectTaskDto.getSummary());
        toCreateProjectTaskEntity.setType(projectTaskDto.getType());
        toCreateProjectTaskEntity.setTaskStartDate(projectTaskDto.getTaskStartDate());
        toCreateProjectTaskEntity.setTaskEndDate(projectTaskDto.getTaskEndDate());

        ProjectTaskEntity savedProjectTaskEntity = projectTaskDao.save(toCreateProjectTaskEntity);

        // 타스크 <> 유저 매핑 정보 저장
        projectTaskUserMapping(savedProjectTaskEntity.getId(), users);

        return ProjectTaskDTO.convert(savedProjectTaskEntity);
    }

    /**
     * 5. 프로젝트 타스크/이슈 수정 - ProjectTaskController
     * @param projectTaskDto
     * @param users
     * @return
     */
    @Transactional
    public ProjectTaskDTO updateProjectTask(ProjectTaskDTO projectTaskDto, List<UserDTO> users){
        if(!validateProjectTaskDate(projectTaskDto)) {
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
            for(ProjectTaskUserEntity mappingEntity : byTaskId){
                projectTaskUserDao.delete(mappingEntity);
            }
            // 타스크 <> 유저 매핑 정보 저장
            projectTaskUserMapping(projectTaskDto.getId(), users);
        }

        toUpdateOne.setProjectId(projectTaskDto.getProjectId());
        toUpdateOne.setProjectTaskName(projectTaskDto.getProjectTaskName());
        toUpdateOne.setParentTaskId(projectTaskDto.getParentTaskId());
        toUpdateOne.setTaskPriority(projectTaskDto.getTaskPriority());
        toUpdateOne.setSummary(projectTaskDto.getSummary());
        toUpdateOne.setType(projectTaskDto.getType());
        toUpdateOne.setTaskStartDate(projectTaskDto.getTaskStartDate());
        toUpdateOne.setTaskEndDate(projectTaskDto.getTaskEndDate());

        return ProjectTaskDTO.convert(projectTaskDao.save(toUpdateOne));
    }

    /**
     * 타스크 <> 유저 매핑
     * @param projectTaskId
     * @param users
     */
    private void projectTaskUserMapping(Long projectTaskId, List<UserDTO> users) {
        for(UserDTO user : users){
            ProjectTaskUserEntity projectTaskUserEntity = new ProjectTaskUserEntity();
            UserEntity userEntity = userDao.getOne(user.getId());

            projectTaskUserEntity.setTaskId(projectTaskId);
            projectTaskUserEntity.setUserId(user.getId());
            projectTaskUserEntity.setUserPosition(userEntity.getUserPosition());

            projectTaskUserDao.save(projectTaskUserEntity);
        }
    }

    /**
     * 타스크 시작일, 종료일 날짜 체크
     * @param projectTaskDto
     * @return
     */
    private boolean validateProjectTaskDate(ProjectTaskDTO projectTaskDto) {
        return projectTaskDto.getTaskEndDate().isAfter(projectTaskDto.getTaskStartDate());
    }

    /**
     * 특정 타스크의 유저 리스트 조회 - UserController
     * @param taskId
     * @return
     */
    public List<UserDTO> getProjectTaskUserIdList(Long taskId) {
        List<UserDTO> users = new ArrayList<>();
        List<ProjectTaskUserEntity> byTaskId = projectTaskUserDao.findAllByTaskId(taskId);

        for(ProjectTaskUserEntity projectTaskUserEntity : byTaskId){
            Optional<UserEntity> user = userDao.findById(projectTaskUserEntity.getUserId());

            UserDTO userDTO = UserDTO.convert(user.get());
            users.add(userDTO);
        }

        return users;
    }
}
