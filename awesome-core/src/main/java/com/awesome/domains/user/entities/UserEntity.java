package com.awesome.domains.user.entities;

import com.awesome.domains.project.enums.ProjectStatus;
import com.awesome.domains.user.enums.UserPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
@ToString
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 성명
     */
    @Column(nullable = false, name = "user_name")
    private String userName;

    /**
     * 직책
     */
    @Column(nullable = false, name = "user_position")
    @Enumerated(EnumType.STRING)
    private UserPosition userPosition;

    /**
     * 년차
     */
    @Column(nullable = true, name = "user_year")
    private long userYear;

    @Column(nullable = true, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;
}
