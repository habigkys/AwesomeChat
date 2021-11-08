package com.awesome.domains.chat.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomDAO extends JpaRepository<ChatRoomEntity, Long> {
    ChatRoomEntity findByRoomId(String roomId);
}
