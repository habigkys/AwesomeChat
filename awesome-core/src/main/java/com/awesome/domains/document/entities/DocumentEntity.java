package com.awesome.domains.document.entities;

import com.awesome.domains.document.enums.DocumentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "document")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 산출물 소속 프로젝트
     */
    @Column(nullable = false, name = "project_id")
    private Long projectId;

    /**
     * 산출물 종류
     */
    @Column(nullable = false, name = "document_type")
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @CreatedDate
    @Column(nullable = true, name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;
}
