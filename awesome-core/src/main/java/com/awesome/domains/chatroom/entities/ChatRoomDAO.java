package com.awesome.domains.chatroom.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChatRoomDAO extends JpaRepository<ChatRoomEntity, Long>, JpaSpecificationExecutor<ChatRoomEntity> {
    ChatRoomEntity findByRoomId(String roomId);
    List<ChatRoomEntity> findAllByRoomCreatorUserId(String userId);
    List<ChatRoomEntity> findAllByRoomIds(List<String> roomIds);
    void deleteAllById(String id);
}
