package com.awesome.domains.chatroom.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChatRoomMessageDAO extends JpaRepository<ChatRoomMessageEntity, Long>, JpaSpecificationExecutor<ChatRoomMessageEntity> {
}
