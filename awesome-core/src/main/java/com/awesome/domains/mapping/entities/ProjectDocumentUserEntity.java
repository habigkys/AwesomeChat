package com.awesome.domains.mapping.entities;

import com.awesome.domains.document.enums.DocumentType;
import com.awesome.domains.user.enums.UserPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 프로젝트 산출물 <> 유저 매핑
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_document_user")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class ProjectDocumentUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 프로젝트 ID
     */
    @Column(nullable = false, name = "project_id")
    private Long taskId;

    /**
     * 프로젝트명
     */
    @Column(nullable = true, name = "project_name")
    private String projectName;

    /**
     * Document ID
     */
    @Column(nullable = false, name = "document_id")
    private Long documentId;

    /**
     * 산출물 종류
     */
    @Column(nullable = false, name = "document_type")
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    /**
     * 유저 ID
     */
    @Column(nullable = false, name = "user_id")
    private Long userId;

    /**
     * 유저 성명
     */
    @Column(nullable = false, name = "user_name")
    private String userName;

    @CreatedDate
    @Column(nullable = true, name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;
}
