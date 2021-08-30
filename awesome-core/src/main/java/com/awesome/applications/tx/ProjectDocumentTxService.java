package com.awesome.applications.tx;

import com.awesome.domains.document.dtos.DocumentDTO;
import com.awesome.domains.document.entities.DocumentDAO;
import com.awesome.domains.document.entities.DocumentEntity;
import com.awesome.domains.mapping.entities.DocumentUserDAO;
import com.awesome.domains.mapping.entities.DocumentUserEntity;
import com.awesome.domains.mapping.entities.ProjectUserDAO;
import com.awesome.domains.project.entities.ProjectDAO;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectDocumentTxService {
    private ProjectDAO projectDao;
    private UserDAO userDao;
    private ProjectUserDAO projectUserDao;
    private DocumentDAO documentDao;
    private DocumentUserDAO documentUserDao;

    /**
     * 7. 프로젝트 산출물 등록 - ProjectController
     * @param documentDTO
     * @param userIds
     * @return
     */
    @Transactional
    public DocumentEntity save(DocumentDTO documentDTO, List<Long> userIds){
        DocumentEntity savedDocumentEntity = documentDao.save(DocumentDTO.convertDtoToEntity(documentDTO));

        // 산출물 <> 유저 매핑 정보 저장
        documentUserMapping(savedDocumentEntity.getId(), userIds);

        return savedDocumentEntity;
    }

    /**
     * 산출물 <> 유저 매핑
     * @param documentId
     * @param userIds
     */
    private void documentUserMapping(Long documentId, List<Long> userIds) {
        List<DocumentUserEntity> documentUserEntities = Lists.newArrayList();
        DocumentEntity documentEntity = documentDao.getOne(documentId);

        List<UserEntity> userEntities = userDao.findAllById(userIds);

        Map<Long, UserEntity> recentUserMap = userEntities.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

        for(Long userId : userIds) {
            DocumentUserEntity documentUserEntity = new DocumentUserEntity();
            documentUserEntity.setDocumentId(documentId);
            documentUserEntity.setDocumentType(documentEntity.getDocumentType());

            UserEntity recentUserEntity = recentUserMap.get(userId);
            documentUserEntity.setUserId(userId);
            documentUserEntity.setUserName(recentUserEntity.getUserName());

            documentUserEntities.add(documentUserEntity);
        }

        documentUserDao.saveAll(documentUserEntities);
    }
}
