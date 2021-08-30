package com.awesome.applications.service;

import com.awesome.applications.tx.ProjectDocumentTxService;
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
    private ProjectDocumentTxService projectDocumentTxService;

    /**
     * 7. 프로젝트 산출물 등록 - ProjectController
     * @param documentDTO
     * @param userIds
     * @return
     */
    @Transactional
    public DocumentDTO createProjectDocuments(DocumentDTO documentDTO, List<Long> userIds){
        // Map은 Param으로 넘기지 않음 보통.
        // Project 1 : Document N : Users M

        // 산출물 담당자 존재해야함
        if(CollectionUtils.isEmpty(userIds)){
            throw new AwesomeException(AwesomeExceptionType.EMPTY_DOCUMENT_USER);
        }

        return DocumentDTO.convertEntityToDto(projectDocumentTxService.save(documentDTO, userIds));
    }
}
