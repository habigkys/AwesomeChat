package com.awesome.domains.document.dtos;

import com.awesome.domains.document.entities.DocumentEntity;
import com.awesome.domains.document.enums.DocumentStatus;
import com.awesome.domains.document.enums.DocumentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DocumentDTO {
    private Long id;

    private Long projectId;

    private DocumentType documentType;

    private DocumentStatus documentStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updatedAt;

    public static DocumentDTO convertEntityToDto(DocumentEntity documentEntity) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(documentEntity.getId());
        dto.setProjectId(documentEntity.getProjectId());
        dto.setDocumentType(documentEntity.getDocumentType());
        dto.setDocumentStatus(documentEntity.getDocumentStatus());
        return dto;
    }

    public static DocumentEntity convertDtoToEntity(DocumentDTO documentDTO) {
        DocumentEntity entity = new DocumentEntity();
        entity.setId(documentDTO.getId());
        entity.setProjectId(documentDTO.getProjectId());
        entity.setDocumentType(documentDTO.getDocumentType());
        entity.setDocumentStatus(documentDTO.getDocumentStatus());
        return entity;
    }
}
