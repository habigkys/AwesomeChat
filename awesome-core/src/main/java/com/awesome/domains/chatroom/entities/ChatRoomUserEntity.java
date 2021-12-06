package com.awesome.domains.chatroom.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_room_user")
@EntityListeners(AuditingEntityListener.class)
public class ChatRoomUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roomId")
    private Long roomId;

    @Column(name = "userId")
    private String userId;

    @Column(name = "userName")
    private String userName;

    @CreatedDate
    @Column(name = "regDateTime", nullable = true)
    private LocalDateTime regDateTime;
}
