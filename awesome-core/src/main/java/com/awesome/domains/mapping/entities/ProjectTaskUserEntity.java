package com.awesome.domains.mapping.entities;

import com.awesome.domains.user.enums.UserPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 타스크 <> 유저 매핑
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_task_user")
@ToString
public class ProjectTaskUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 타스크 ID
     */
    @Column(nullable = false, name = "task_id")
    private Long taskId;

    /**
     * 유저 ID
     */
    @Column(nullable = false, name = "user_id")
    private Long userId;

    /**
     * 유저 직책
     */
    @Column(nullable = false, name = "user_position")
    private UserPosition userPosition;

    @Column(nullable = true, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;
}
