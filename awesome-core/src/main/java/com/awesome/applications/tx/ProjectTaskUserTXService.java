package com.awesome.applications.tx;

import com.awesome.domains.mapping.entities.ProjectTaskUserDAO;
import com.awesome.domains.mapping.entities.ProjectTaskUserEntity;
import com.awesome.domains.projecttask.entities.ProjectTaskDAO;
import com.awesome.domains.projecttask.entities.ProjectTaskEntity;
import com.awesome.domains.user.entities.UserDAO;
import com.awesome.domains.user.entities.UserEntity;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectTaskUserTXService {
    private ProjectTaskDAO projectTaskDao;
    private UserDAO userDao;
    private ProjectTaskUserDAO projectTaskUserDao;

    /**
     * 4. 프로젝트 타스크/이슈 생성 - ProjectTaskUserService
     * @param projectTaskEntity
     * @param userIds
     * @return
     */
    @Transactional
    public ProjectTaskEntity save(ProjectTaskEntity projectTaskEntity, List<Long> userIds){
        ProjectTaskEntity savedProjectTaskEntity = projectTaskDao.save(projectTaskEntity);

        // 타스크 <> 유저 매핑 정보 저장
        projectTaskUserMapping(savedProjectTaskEntity.getId(), userIds);

        return savedProjectTaskEntity;
    }

    /**
     * 5. 프로젝트 타스크/이슈 수정 - ProjectTaskController
     * @param projectTaskEntity
     * @param userIds
     * @return
     */
    @Transactional
    public ProjectTaskEntity update(ProjectTaskEntity projectTaskEntity, List<Long> userIds){
        // 기존 매핑 정보 삭제 후
        projectTaskUserDao.deleteAllByTaskId(projectTaskEntity.getId());

        return save(projectTaskEntity, userIds);
    }

    /**
     * 타스크 <> 유저 매핑
     * @param projectTaskId
     * @param userIds
     */
    private void projectTaskUserMapping(Long projectTaskId, List<Long> userIds) {
        List<ProjectTaskUserEntity> projectTaskUserEntities = Lists.newArrayList();

        List<UserEntity> userEntities = userDao.findAllById(userIds);
        Map<Long, UserEntity> recentUserMap = userEntities.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

        for(Long userId : userIds){
            ProjectTaskUserEntity projectTaskUserEntity = new ProjectTaskUserEntity();
            projectTaskUserEntity.setTaskId(projectTaskId);
            projectTaskUserEntity.setUserId(userId);

            UserEntity recentUserEntity = recentUserMap.get(userId);
            projectTaskUserEntity.setUserPosition(recentUserEntity.getUserPosition());

            projectTaskUserEntities.add(projectTaskUserEntity);
        }

        projectTaskUserDao.saveAll(projectTaskUserEntities);
    }
}
