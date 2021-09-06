package com.awesome.applications.service;

import com.awesome.applications.tx.ProjectDocumentTxService;
import com.awesome.domains.document.dtos.DocumentDTO;
import com.awesome.infrastructures.exceptions.AwesomeException;
import com.awesome.infrastructures.exceptions.AwesomeExceptionType;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        return DocumentDTO.convertEntityToDto(projectDocumentTxService.save(DocumentDTO.convertDtoToEntity(documentDTO), userIds));
    }
}
