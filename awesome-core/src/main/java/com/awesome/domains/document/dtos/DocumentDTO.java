package com.awesome.domains.document.dtos;

import com.awesome.domains.document.entities.DocumentEntity;
import com.awesome.domains.document.enums.DocumentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DocumentDTO {
    private Long id;

    private DocumentType documentType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static DocumentDTO convert(DocumentEntity documentEntity) {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(documentEntity.getId());
        documentDTO.setDocumentType(documentEntity.getDocumentType());
        return documentDTO;
    }
}
