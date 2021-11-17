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
@EntityListeners(AuditingEntityListener.class)
public class ChatRoomMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomId;

    @Column
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column
    private String messageSendUserId;

    @Column
    private String message;

    @CreatedDate
    @Column(nullable = true)
    private LocalDateTime regDateTime;
}
