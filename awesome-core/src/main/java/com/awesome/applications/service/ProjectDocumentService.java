package com.awesome.applications.service;

import com.awesome.domains.document.dtos.DocumentDTO;
import com.awesome.domains.document.entities.DocumentDAO;
import com.awesome.domains.document.entities.DocumentEntity;
import com.awesome.domains.mapping.entities.DocumentUserDAO;
import com.awesome.domains.mapping.entities.DocumentUserEntity;
import com.awesome.domains.mapping.entities.ProjectUserDAO;
import com.awesome.domains.project.entities.ProjectDAO;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProjectDocumentService {
    private ProjectDAO projectDao;
    private UserDAO userDao;
    private ProjectUserDAO projectUserDao;
    private DocumentDAO documentDao;
    private DocumentUserDAO documentUserDao;

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
