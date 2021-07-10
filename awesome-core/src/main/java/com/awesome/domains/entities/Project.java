package com.awesome.domains.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
public class Project {

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
    @Column(nullable = true, name = "summray")
    private String summary;

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

    @Column(nullable = true, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Project { id: " + id
                + ", projectName: " + projectName
                + ", summary: " + summary
                + ", startDate: " + startDate
                + ", endDate: " + endDate
                + ", createdAt: " + createdAt
                + ", updatedAt: " + updatedAt
                + " }";
    }
}
