package com.awesome.domains.chat.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String roomId;

    @Column(nullable = false)
    private String roomCreatorUserId;

    @Column
    private String roomName;

    @Column
    private Long roomMaxUserNum;

    @Column
    private Long roomCurUserNum;

    @CreatedDate
    @Column(nullable = true)
    private LocalDateTime regDateTime;

    @LastModifiedDate
    @Column(nullable = true)
    private LocalDateTime updDateTime;
}
