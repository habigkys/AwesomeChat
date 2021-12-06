package com.awesome.domains.chatroom.entities;

import com.awesome.domains.chatroom.enums.MessageType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "chat_room_message")
@EntityListeners(AuditingEntityListener.class)
public class ChatRoomMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roomId", nullable = false)
    private Long roomId;

    @Column(name = "messageType")
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column(name = "messageSendUserId")
    private String messageSendUserId;

    @Column(name = "messageSendUserName")
    private String messageSendUserName;

    @Column(name = "message")
    private String message;

    @CreatedDate
    @Column(name = "regDateTime", nullable = true)
    private LocalDateTime regDateTime;
}
