package com.awesome.applications.tx;

import com.awesome.domains.document.dtos.DocumentDTO;
import com.awesome.domains.document.entities.DocumentDAO;
import com.awesome.domains.document.entities.DocumentEntity;
import com.awesome.domains.mapping.entities.DocumentUserDAO;
import com.awesome.domains.mapping.entities.DocumentUserEntity;
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
public class ProjectDocumentTXService {
    private ProjectDAO projectDao;
    private UserDAO userDao;
    private ProjectUserDAO projectUserDao;
    private DocumentDAO documentDao;
    private DocumentUserDAO documentUserDao;

    /**
     * 4. 프로젝트 생성 - ProjectController
     * @param projectDto
     * @param userIds
     * @return
     */
    @Transactional
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

        ProjectEntity savedProjectEntity = projectDao.save(ProjectDTO.convertDtoToEntity(projectDto));

        // 프로젝트 <> 유저 매핑 정보 저장
        projectUserMapping(savedProjectEntity.getId(), userIds);

        return ProjectDTO.convertEntityToDto(savedProjectEntity);
    }

    /**
     * 5. 프로젝트 수정 - ProjectController
     * @param projectDto
     * @param userIds
     * @return
     */
    @Transactional
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

            // 기존 매핑 정보 삭제 후
            projectUserDao.deleteAllByProjectId(projectDto.getId());

            // 프로젝트 <> 유저 매핑 정보 저장
            projectUserMapping(projectDto.getId(), userIds);
        }

        return ProjectDTO.convertEntityToDto(projectDao.save(ProjectDTO.convertDtoToEntity(projectDto)));
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

    /**
     * 7. 프로젝트 산출물 등록 - ProjectController
     * @param documentDTO
     * @param documentUsers
     * @return
     */
    @Transactional
    public DocumentDTO createProjectDocuments(DocumentDTO documentDTO, List<UserDTO> documentUsers){
        // Map은 Param으로 넘기지 않음 보통.
        // Project 1 : Document N : Users M

        // 산출물 담당자 존재해야함
        if(CollectionUtils.isEmpty(documentUsers)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_DOCUMENT_USER);
        }

        DocumentEntity savedDocumentEntity = documentDao.save(DocumentDTO.convertDtoToEntity(documentDTO));

        // 산출물 <> 유저 매핑 정보 저장
        documentUserMapping(savedDocumentEntity.getId(), documentUsers);

        return DocumentDTO.convertEntityToDto(savedDocumentEntity);
    }

    /**
     * 프로젝트 <> 유저 매핑
     * @param projectId
     * @param userIds
     */
    private void projectUserMapping(Long projectId, List<Long> userIds) {
        List<ProjectUserEntity> projectUserEntities = Lists.newArrayList();
        //준비단계
        //1
        ProjectEntity projectEntity = projectDao.getOne(projectId);

        //1 + CPU < IO
        List<UserEntity> userEntities = userDao.findAllById(userIds);

        Map<Long, UserEntity> recentUserMap = userEntities.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

        //O(N)
        for(Long userId : userIds) {
            ProjectUserEntity projectUserEntity = new ProjectUserEntity();
            projectUserEntity.setProjectId(projectId);
            projectUserEntity.setProjectName(projectEntity.getProjectName());

            UserEntity recentUserEntity = recentUserMap.get(userId);
            projectUserEntity.setUserId(userId);
            projectUserEntity.setUserPosition(recentUserEntity.getUserPosition());
            projectUserEntity.setUserName(recentUserEntity.getUserName());

            projectUserEntities.add(projectUserEntity);
        }

        //O(3N)
        //
        projectUserDao.saveAll(projectUserEntities);
    }

    /**
     * 산출물 <> 유저 매핑
     * @param documentId
     * @param users
     */
    private void documentUserMapping(Long documentId, List<UserDTO> users) {
        List<DocumentUserEntity> documentUserEntities = Lists.newArrayList();
        DocumentEntity documentEntity = documentDao.getOne(documentId);

        List<UserEntity> userEntities = userDao.findAllById(users.stream().map(e -> e.getId()).collect(Collectors.toList()));

        Map<Long, UserEntity> recentUserMap = userEntities.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

        for(UserDTO user : users) {
            DocumentUserEntity documentUserEntity = new DocumentUserEntity();
            documentUserEntity.setDocumentId(documentId);
            documentUserEntity.setDocumentType(documentEntity.getDocumentType());

            UserEntity recentUserEntity = recentUserMap.get(user.getId());
            documentUserEntity.setUserId(user.getId());
            documentUserEntity.setUserName(recentUserEntity.getUserName());

            documentUserEntities.add(documentUserEntity);
        }

        documentUserDao.saveAll(documentUserEntities);
    }
}
