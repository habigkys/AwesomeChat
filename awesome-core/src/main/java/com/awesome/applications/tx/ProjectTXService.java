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
import com.awesome.domains.projecttask.entities.ProjectTaskDAO;
import com.awesome.domains.user.dtos.UserDTO;
import com.awesome.domains.user.entities.UserDAO;
import com.awesome.domains.user.entities.UserEntity;
import com.awesome.domains.user.enums.UserPosition;
import com.awesome.infrastructures.AwesomeException;
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
     * @param projectDto, userDto
     * @return
     */
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDto, List<Long> userIds){
        if(!validateProjectDate(projectDto)) {
            throw new AwesomeException("시작일은 종료일보다 뒤에 올 수 없습니다.");
        }

        // 프로젝트 참여인력이 없으면 안됨
        if(CollectionUtils.isEmpty(userIds)){
            throw new AwesomeException("프로젝트 참여인력을 포함해 주세요.");
        }

        // 프로젝트 리더는 1명을 초과할 수 없음
        if(getLeaderCnt(projectDto.getId()) > 1){
            throw new AwesomeException("프로젝트 리더는 1명을 초과할 수 없습니다.");
        }

        // 과거 또는 현재의 프로젝트 생성시 TODO 불가
        if((LocalDate.now().isAfter(projectDto.getEndDate()) || LocalDate.now().isAfter(projectDto.getStartDate()) && LocalDate.now().isBefore(projectDto.getEndDate()))
                && ProjectStatus.TODO.equals(projectDto.getStatus())){
            throw new AwesomeException("설정된 프로젝트 기간은 해야 함 프로젝트로 등록할 수 없습니다.");
        }

        ProjectEntity toCreateProjectEntity = new ProjectEntity();

        toCreateProjectEntity.setProjectName(projectDto.getProjectName());
        toCreateProjectEntity.setSummary(projectDto.getSummary());
        toCreateProjectEntity.setStatus(projectDto.getStatus());
        toCreateProjectEntity.setStartDate(projectDto.getStartDate());
        toCreateProjectEntity.setEndDate(projectDto.getEndDate());
        toCreateProjectEntity.setCreatedAt(LocalDateTime.now());
        toCreateProjectEntity.setUpdatedAt(LocalDateTime.now());

        ProjectEntity savedProjectEntity = projectDao.save(toCreateProjectEntity);

        // 프로젝트 <> 유저 매핑 정보 저장
        projectUserMapping(savedProjectEntity.getId(), userIds);

        return ProjectDTO.convert(savedProjectEntity);
    }

    /**
     * 5. 프로젝트 수정 - ProjectController
     * @param projectDto
     * @return
     */
    @Transactional
    public ProjectDTO updateProject(ProjectDTO projectDto, Long projectId, List<Long> userIds){
        if(!validateProjectDate(projectDto)) {
            throw new AwesomeException("시작일은 종료일보다 뒤에 올 수 없습니다.");
        }

        Optional<ProjectEntity> byId = projectDao.findById(projectId);

        ProjectEntity toUpdateOne = byId.get();

        // TODO 상태가 아닌 프로젝트의 상태를 CANCELED로 변경하려고 할 때 생성일부터 일주일 미만인 프로젝트일 때 변경 불가
        if(ProjectStatus.CANCELED.equals(projectDto.getStatus()) && !ProjectStatus.TODO.equals(toUpdateOne.getStatus())
                && ChronoUnit.WEEKS.between(toUpdateOne.getCreatedAt(), LocalDateTime.now()) < 1){
            throw new AwesomeException("프로젝트 생성일이 " + toUpdateOne.getCreatedAt() + " 입니다. 생성일부터 일주일 미만인 프로젝트는 취소상태로 변경할 수 없습니다.");
        }

        // 프로젝트 종료까지 일주일 미만 남았으면 프로젝트 우선순위를 VERYHIGH로 변경 불가
        if(ProjectPriority.VERYHIGH.equals(projectDto.getProjectPriority())
                && ChronoUnit.WEEKS.between(toUpdateOne.getEndDate(), LocalDateTime.now()) < 1){
            throw new AwesomeException("프로젝트 종료일이 " + toUpdateOne.getEndDate() + " 입니다. 종료일까지 일주일 미만인 프로젝트는 우선순위를 매우 높음으로 변경할 수 없습니다.");
        }

        // 프로젝트 종료까지 이주일 미만 남았으면 프로젝트 우선순위를 HIGH로 변경 불가
        if(ProjectPriority.HIGH.equals(projectDto.getProjectPriority())
                && ChronoUnit.WEEKS.between(toUpdateOne.getEndDate(), LocalDateTime.now()) < 2){
            throw new AwesomeException("프로젝트 종료일이 " + toUpdateOne.getEndDate() + " 입니다. 종료일까지 이주일 미만인 프로젝트는 우선순위를 높음으로 변경할 수 없습니다.");
        }

        // 프로젝트 참여인력 변경이 있을 경우
        if(!CollectionUtils.isEmpty(userIds)){
            // 프로젝트 우선순위가 VERYHIGH 또는 HIGH로 급할 경우 인력 교체 불가
            if((ProjectPriority.HIGH.equals(projectDto.getProjectPriority()) || ProjectPriority.VERYHIGH.equals(projectDto.getProjectPriority()))){
                throw new AwesomeException("프로젝트 우선순위가 " + projectDto.getProjectPriority() + " 입니다. 해당 우선순위의 프로젝트는 인력을 변경할 수 없습니다.");
            }

            // 프로젝트 리더는 1명을 초과할 수 없음
            if(getLeaderCnt(projectId) > 1){
                throw new AwesomeException("프로젝트 리더는 1명을 초과할 수 없습니다.");
            }

            List<ProjectUserEntity> byProjectId = projectUserDao.findAllByProjectId(projectDto.getId());

            // 기존 매핑 정보 삭제 후
            for(ProjectUserEntity mappingEntity : byProjectId){
                projectUserDao.delete(mappingEntity);
            }
            // 프로젝트 <> 유저 매핑 정보 저장
            projectUserMapping(projectDto.getId(), userIds);
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
     * 7. 특정 프로젝트의 유저 리스트 조회 - UserController
     * @param projectId
     * @return
     */
    public List<UserDTO> getProjectUserList(Long projectId){
        List<UserDTO> userDTOList = new ArrayList<>();
        List<ProjectUserEntity> byProjectId = projectUserDao.findAllByProjectId(projectId);

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
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        List<ProjectUserEntity> byUserId = projectUserDao.findAllByUserId(userId);

        for(ProjectUserEntity projectUserEntity : byUserId){
            Optional<ProjectEntity> project = projectDao.findById(projectUserEntity.getProjectId());

            ProjectDTO projectDTO = ProjectDTO.convert(project.get());
            projectDTOList.add(projectDTO);
        }

        return projectDTOList;
    }

    /**
     * 7. 프로젝트 산출물 Add - ProjectController
     * @param projectDto
     * @return
     */
    @Transactional
    public void updateProjectDocuments(ProjectDTO projectDto, List<DocumentType> documentTypes){
        // 산출물 추가
        if(!CollectionUtils.isEmpty(documentTypes)){
            for(DocumentType documentType : documentTypes){
                DocumentEntity documentEntity = new DocumentEntity();

                documentEntity.setProjectId(projectDto.getId());
                documentEntity.setDocumentType(documentType);
                documentEntity.setCreatedAt(LocalDateTime.now());

                documentDao.save(documentEntity);
            }
        }
    }

    /**
     * 프로젝트 <> 유저 매핑
     * @param projectId
     * @param userIds
     */
    private void projectUserMapping(Long projectId, List<Long> userIds) {
        for(Long userId : userIds){
            ProjectUserEntity projectUserEntity = new ProjectUserEntity();

            UserEntity userEntity = userDao.getOne(userId);
            ProjectEntity projectEntity = projectDao.getOne(projectId);

            projectUserEntity.setProjectId(projectId);
            projectUserEntity.setProjectName(projectEntity.getProjectName());
            projectUserEntity.setUserId(userId);
            projectUserEntity.setUserPosition(userEntity.getUserPosition());
            projectUserEntity.setUserName(userEntity.getUserName());
            projectUserEntity.setCreatedAt(LocalDateTime.now());

            projectUserDao.save(projectUserEntity);
        }
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
    private int getLeaderCnt(Long projectId) {
        List<ProjectUserEntity> leaderCnt = projectUserDao.findAllByProjectIdAndUserPosition(projectId, UserPosition.LEADER);

        return leaderCnt.size();
    }
}
