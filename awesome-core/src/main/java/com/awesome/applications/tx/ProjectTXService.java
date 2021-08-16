package com.awesome.applications.tx;

import com.awesome.domains.document.entities.DocumentDAO;
import com.awesome.domains.document.entities.DocumentEntity;
import com.awesome.domains.document.enums.DocumentType;
import com.awesome.domains.mapping.entities.ProjectUserDAO;
import com.awesome.domains.mapping.entities.ProjectUserEntity;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.entities.ProjectDAO;
import com.awesome.domains.project.enums.ProjectPriority;
import com.awesome.domains.project.enums.ProjectStatus;
import com.awesome.domains.project.validator.AwesomeProjectHasInvalidDate;
import com.awesome.domains.projecttask.entities.ProjectTaskDAO;
import com.awesome.domains.user.dtos.UserDTO;
import com.awesome.domains.user.entities.UserDAO;
import com.awesome.domains.user.entities.UserEntity;
import com.awesome.domains.user.enums.UserPosition;
import com.awesome.infrastructures.AwesomeException;
import com.awesome.infrastructures.AwesomeExceptionType;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectTXService {
    private ProjectDAO projectDao;
    private ProjectTaskDAO projectTaskDao;
    private UserDAO userDao;
    private ProjectUserDAO projectUserDao;
    private DocumentDAO documentDao;

    /**
     * 4. 프로젝트 생성 - ProjectController
     * @param projectDto
     * @param users
     * @return
     */
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDto, List<UserDTO> users){
        if(!validateProjectDate(projectDto)) {
            throw new AwesomeException(AwesomeExceptionType.WRONG_DATE);
        }

        // 프로젝트 참여인력이 없으면 안됨
        if(CollectionUtils.isEmpty(users)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_PROJECT_USER);
        }

        // 프로젝트 리더는 1명을 초과할 수 없음
        List<UserDTO> leaders = users.stream().filter(e -> UserPosition.LEADER.equals(e.getUserPosition())).collect(Collectors.toList());
        if(leaders.size() > 1){
            throw new AwesomeException(AwesomeExceptionType.MULTI_LEADER);
        }

        // 과거 또는 현재의 프로젝트 생성시 TODO 불가
        if(AwesomeProjectHasInvalidDate.get().validate(projectDto)){
            throw new AwesomeException(AwesomeExceptionType.TODO_DATE_INVALID);
        }

        ProjectEntity toCreateProjectEntity = new ProjectEntity();

        toCreateProjectEntity.setProjectName(projectDto.getProjectName());
        toCreateProjectEntity.setSummary(projectDto.getSummary());
        toCreateProjectEntity.setStatus(projectDto.getStatus());
        toCreateProjectEntity.setProjectPriority(projectDto.getProjectPriority());
        toCreateProjectEntity.setStartDate(projectDto.getStartDate());
        toCreateProjectEntity.setEndDate(projectDto.getEndDate());

        ProjectEntity savedProjectEntity = projectDao.save(toCreateProjectEntity);

        // 프로젝트 <> 유저 매핑 정보 저장
        projectUserMapping(savedProjectEntity.getId(), users);

        return ProjectDTO.convert(savedProjectEntity);
    }

    /**
     * 5. 프로젝트 수정 - ProjectController
     * @param projectDto
     * @param users
     * @return
     */
    @Transactional
    public ProjectDTO updateProject(ProjectDTO projectDto, List<UserDTO> users){
        if(!validateProjectDate(projectDto)) {
            throw new AwesomeException(AwesomeExceptionType.WRONG_DATE);
        }

        Optional<ProjectEntity> byId = projectDao.findById(projectDto.getId());

        if(byId.isEmpty()){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_PROJECT);
        }

        ProjectEntity toUpdateOne = byId.get();

        // TODO 상태가 아닌 프로젝트의 상태를 CANCELED로 변경하려고 할 때 생성일부터 일주일 미만인 프로젝트일 때 변경 불가
        if(ProjectStatus.CANCELED.equals(projectDto.getStatus()) && !ProjectStatus.TODO.equals(toUpdateOne.getStatus())
                && ChronoUnit.WEEKS.between(toUpdateOne.getCreatedAt(), LocalDateTime.now()) < 1){
            throw new AwesomeException(AwesomeExceptionType.ONE_WEEK_CANCEL);
        }

        // 프로젝트 종료까지 일주일 미만 남았으면 프로젝트 우선순위를 VERYHIGH로 변경 불가
        if(ProjectPriority.VERYHIGH.equals(projectDto.getProjectPriority())
                && ChronoUnit.WEEKS.between(toUpdateOne.getEndDate(), LocalDateTime.now()) < 1){
            throw new AwesomeException(AwesomeExceptionType.ONE_WEEK_PRIORITY);
        }

        // 프로젝트 종료까지 이주일 미만 남았으면 프로젝트 우선순위를 HIGH로 변경 불가
        if(ProjectPriority.HIGH.equals(projectDto.getProjectPriority())
                && ChronoUnit.WEEKS.between(toUpdateOne.getEndDate(), LocalDateTime.now()) < 2){
            throw new AwesomeException(AwesomeExceptionType.TWO_WEEK_PRIORITY);
        }

        // 프로젝트 참여인력 변경이 있을 경우
        if(!CollectionUtils.isEmpty(users)){
            // 프로젝트 우선순위가 VERYHIGH 또는 HIGH로 급할 경우 인력 교체 불가
            if((ProjectPriority.HIGH.equals(projectDto.getProjectPriority()) || ProjectPriority.VERYHIGH.equals(projectDto.getProjectPriority()))){
                throw new AwesomeException(AwesomeExceptionType.HIGH_PRIORITY_USER_CHANGE);
            }

            // 프로젝트 리더는 1명을 초과할 수 없음
            if(getLeaderCnt(projectDto.getId()) > 1){
                throw new AwesomeException(AwesomeExceptionType.MULTI_LEADER);
            }

            List<ProjectUserEntity> byProjectId = projectUserDao.findAllByProjectId(projectDto.getId());

            // 기존 매핑 정보 삭제 후
            for(ProjectUserEntity mappingEntity : byProjectId){
                projectUserDao.delete(mappingEntity);
            }
            // 프로젝트 <> 유저 매핑 정보 저장
            projectUserMapping(projectDto.getId(), users);
        }

        toUpdateOne.setProjectName(projectDto.getProjectName());
        toUpdateOne.setSummary(projectDto.getSummary());
        toUpdateOne.setStatus(projectDto.getStatus());
        toUpdateOne.setStartDate(projectDto.getStartDate());
        toUpdateOne.setEndDate(projectDto.getEndDate());

        return ProjectDTO.convert(projectDao.save(toUpdateOne));
    }

    /**
     * 7. 특정 프로젝트의 유저 리스트 조회 - UserController
     * @param projectId
     * @return
     */
    public List<UserDTO> getProjectUserList(Long projectId){
        List<UserDTO> userDTOList = new ArrayList<>();
        List<ProjectUserEntity> byProjectId = projectUserDao.findAllByProjectId(projectId);

        if(byProjectId.isEmpty()){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_PROJECT);
        }

        for(ProjectUserEntity projectUserEntity : byProjectId){
            Optional<UserEntity> user = userDao.findById(projectUserEntity.getUserId());

            UserDTO userDTO = UserDTO.convert(user.get());
            userDTOList.add(userDTO);
        }

        return userDTOList;
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

        List<ProjectEntity> usersProjects = projectDao.findAllById(projectIds);
        return usersProjects.stream().map(ProjectDTO::convert).collect(Collectors.toList());
    }

    /**
     * 7. 프로젝트 산출물 Add - ProjectController
     * @param projectDto
     * @return
     */
    @Transactional
    public void updateProjectDocuments(ProjectDTO projectDto, List<DocumentType> documentTypes){
        // 산출물 추가
        if(CollectionUtils.isEmpty(documentTypes)) {
            return;
        }
        documentDao.saveAll(documentTypes.stream().map(e -> {
            DocumentEntity entity = new DocumentEntity();
            entity.setProjectId(projectDto.getId());
            entity.setDocumentType(e);
            return entity;
        }).collect(Collectors.toList()));
    }

    /**
     * 프로젝트 <> 유저 매핑
     * @param projectId
     * @param users
     */
    private void projectUserMapping(Long projectId, List<UserDTO> users) {
        List<ProjectUserEntity> projectUserEntities = Lists.newArrayList();
        //준비단계
        //1
        ProjectEntity projectEntity = projectDao.getOne(projectId);

        //1 + CPU < IO
        List<UserEntity> userEntities = userDao.findAllById(users.stream().map(e -> e.getId()).collect(Collectors.toList()));

        Map<Long, UserEntity> recentUserMap = userEntities.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

        //O(N)
        for(UserDTO user : users) {
            ProjectUserEntity projectUserEntity = new ProjectUserEntity();
            projectUserEntity.setProjectId(projectId);
            projectUserEntity.setProjectName(projectEntity.getProjectName());

            UserEntity recentUserEntity = recentUserMap.get(user.getId());
            projectUserEntity.setUserId(user.getId());
            projectUserEntity.setUserPosition(recentUserEntity.getUserPosition());
            projectUserEntity.setUserName(recentUserEntity.getUserName());

            projectUserEntities.add(projectUserEntity);
        }

        //O(3N)
        //
        projectUserDao.saveAll(projectUserEntities);
    }

    /**
     * 프로젝트 시작일, 종료일 날짜 체크
     * @param projectDto
     * @return
     */
    private boolean validateProjectDate(ProjectDTO projectDto) {
        return projectDto.getEndDate().isAfter(projectDto.getStartDate());
    }

    /**
     * 참여인력 중 리더 포지션 카운트
     * @param projectId
     * @return
     */
    private Long getLeaderCnt(Long projectId) {
        return projectUserDao.countByProjectIdAndUserPosition(projectId, UserPosition.LEADER);
    }

    /**
     * 특정 프로젝트의 유저 리스트 조회 - UserController
     * @param projectId
     * @return
     */
    public List<UserDTO> getProjectUserIdList(Long projectId) {
        List<ProjectUserEntity> byProjectId = projectUserDao.findAllByProjectId(projectId);

        List<Long> userIds = byProjectId.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        return userDao.findAllById(userIds).stream().map(UserDTO::convert).collect(Collectors.toList());

    }
}
