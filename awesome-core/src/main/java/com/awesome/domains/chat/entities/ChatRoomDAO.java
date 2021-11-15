package com.awesome.domains.chat.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomDAO extends JpaRepository<ChatRoomEntity, Long> {
    ChatRoomEntity findById(String id);
    List<ChatRoomEntity> findAllByRoomCreatorUserId(String userId);
    void deleteAllById(String id);
}
