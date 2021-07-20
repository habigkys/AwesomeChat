package com.awesome.domains.mapping.entities;

import com.awesome.domains.document.enums.DocumentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_document")
@ToString
public class ProjectDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 프로젝트 ID
     */
    @Column(nullable = false, name = "project_id")
    private Long projectId;

    /**
     * 프로젝트명
     */
    @Column(nullable = true, name = "project_name")
    private String projectName;

    /**
     * 산출물 ID
     */
    @Column(nullable = false, name = "document_id")
    private Long documentId;

    /**
     * 산출물 타입
     */
    @Column(nullable = false, name = "user_name")
    private DocumentType documentType;

    @Column(nullable = true, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;
}
