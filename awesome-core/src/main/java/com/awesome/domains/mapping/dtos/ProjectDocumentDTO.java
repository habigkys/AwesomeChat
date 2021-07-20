package com.awesome.domains.mapping.dtos;

import com.awesome.domains.document.enums.DocumentType;
import com.awesome.domains.mapping.entities.ProjectDocumentEntity;
import com.awesome.domains.user.entities.UserEntity;
import com.awesome.domains.user.enums.UserPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDocumentDTO {
    private Long id;

    private Long projectId;

    private String projectName;

    private Long documentId;

    private DocumentType documentType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ProjectDocumentDTO convert(ProjectDocumentEntity projectDocumentEntity) {
        ProjectDocumentDTO projectDocumentDTO = new ProjectDocumentDTO();
        projectDocumentDTO.setProjectId(projectDocumentEntity.getProjectId());
        projectDocumentDTO.setProjectName(projectDocumentDTO.getProjectName());
        projectDocumentDTO.setDocumentId(projectDocumentDTO.getDocumentId());
        projectDocumentDTO.setDocumentType(projectDocumentDTO.getDocumentType());
        return projectDocumentDTO;
    }
}
