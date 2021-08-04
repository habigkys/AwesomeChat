package com.awesome.domains.user.entities;

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

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
@ToString
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(nullable = true, name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;
}
