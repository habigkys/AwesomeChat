package com.awesome.applications.tx;

import com.awesome.domains.document.dtos.DocumentDTO;
import com.awesome.domains.document.entities.DocumentDAO;
import com.awesome.domains.document.entities.DocumentEntity;
import com.awesome.domains.document.enums.DocumentType;
import com.awesome.domains.mapping.entities.DocumentUserDAO;
import com.awesome.domains.mapping.entities.DocumentUserEntity;
import com.awesome.domains.mapping.entities.ProjectUserDAO;
import com.awesome.domains.mapping.entities.ProjectUserEntity;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.awesome.domains.project.entities.ProjectDAO;
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

import javax.swing.text.Document;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectUserTXService {
    private ProjectDAO projectDao;
    private UserDAO userDao;
    private ProjectUserDAO projectUserDao;

    /**
     * 프로젝트 생성 - ProjectUserService
     * @param projectDto
     * @param userIds
     * @return
     */
    @Transactional
    public ProjectEntity save(ProjectDTO projectDto, List<Long> userIds){
        ProjectEntity savedProjectEntity = projectDao.save(ProjectDTO.convertDtoToEntity(projectDto));

        // 프로젝트 <> 유저 매핑 정보 저장
        projectUserMapping(savedProjectEntity.getId(), userIds);

        return savedProjectEntity;
    }

    /**
     * 5. 프로젝트 수정 - ProjectController
     * @param projectDto
     * @param userIds
     * @return
     */
    @Transactional
    public ProjectEntity update(ProjectDTO projectDto, List<Long> userIds){
        // 기존 매핑 정보 삭제 후
        projectUserDao.deleteAllByProjectId(projectDto.getId());

        // 프로젝트 <> 유저 매핑 정보 저장
        projectUserMapping(projectDto.getId(), userIds);

        return projectDao.save(ProjectDTO.convertDtoToEntity(projectDto));
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
}
