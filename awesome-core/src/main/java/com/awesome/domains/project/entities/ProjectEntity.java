package com.awesome.domains.project.entities;

import com.awesome.domains.project.enums.ProjectPriority;
import com.awesome.domains.project.enums.ProjectStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 프로젝트명
     */
    @Column(nullable = false, name = "project_name")
    private String projectName;

    /**
     * 프로젝트 요약
     */
    @Column(nullable = true, name = "summary")
    private String summary;

    /**
     * 프로젝트 상태
     */
    @Column(nullable = false, name = "project_status")
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    /**
     * 프로젝트 우선순위
     */
    @Column(nullable = false, name = "project_priority")
    @Enumerated(EnumType.STRING)
    private ProjectPriority projectPriority;

    /**
     * 프로젝트 시작 시간
     */
    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    /**
     * 프로젝트 종료 시간
     */
    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;

    @CreatedDate
    @Column(nullable = true, name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;
}
