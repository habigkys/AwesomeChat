package com.awesome.domains.mapping.entities;

import com.awesome.domains.user.enums.UserPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 프로젝트 <> 유저 매핑
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_user")
@ToString
public class ProjectUserEntity {

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
     * 유저 ID
     */
    @Column(nullable = false, name = "user_id")
    private Long userId;

    /**
     * 유저 직책
     */
    @Column(nullable = false, name = "user_position")
    private UserPosition userPosition;

    /**
     * 유저 성명
     */
    @Column(nullable = false, name = "user_name")
    private String userName;

    @Column(nullable = true, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;
}
