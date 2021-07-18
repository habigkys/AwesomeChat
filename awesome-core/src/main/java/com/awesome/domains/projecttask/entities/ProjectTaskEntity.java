package com.awesome.domains.projecttask.entities;

import com.awesome.domains.projecttask.enums.TaskType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "project_task")
@ToString
public class ProjectTaskEntity {
    /**
     * 타스크 아이디
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 타스크 소속 프로젝트
     */
    @Column(nullable = false, name = "project_id")
    private Long projectId;

    /**
     * 타스크 소속 타스크(이슈)
     */
    @Column(nullable = false, name = "parent_task_id")
    private Long parentTaskId;

    /**
     * 타스크 요약
     */
    @Column(nullable = false, name = "summary")
    private String summary;

    /**
     * 타스크 시작일
     */
    @Column(nullable = false, name = "task_start_date")
    private LocalDate taskStartDate;

    /**
     * 타스크 종료일
     */
    @Column(nullable = false, name = "task_end_date")
    private LocalDate taskEndDate;

    /**
     * 타스크 참여인력
     */
    @Column(nullable = false, name = "persons")
    private String persons;

    /**
     * 타스크의 스코프
     */
    @Column(nullable = false, name = "type")
    private TaskType type;

    @Column(nullable = true, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;
}
