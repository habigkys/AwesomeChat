package com.awesome.domains.chatroom.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_room")
@EntityListeners(AuditingEntityListener.class)
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roomCreatorUserId", nullable = false)
    private String roomCreatorUserId;

    @Column(name = "roomCreatorUserName")
    private String roomCreatorUserName;

    @Column(name = "roomName")
    private String roomName;

    @Column(name = "roomMaxUserNum")
    private Long roomMaxUserNum;

    @Column(name = "roomCurUserNum")
    private Long roomCurUserNum;

    @CreatedDate
    @Column(name = "regDateTime", nullable = true)
    private LocalDateTime regDateTime;

    @LastModifiedDate
    @Column(name = "updDateTime", nullable = true)
    private LocalDateTime updDateTime;
}
