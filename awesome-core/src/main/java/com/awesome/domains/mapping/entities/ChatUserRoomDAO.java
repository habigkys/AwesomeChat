package com.awesome.domains.mapping.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatUserRoomDAO extends JpaRepository<ChatUserRoomEntity, Long> {
    List<ChatUserRoomEntity> findAllByRoomId(String roomId);
    List<ChatUserRoomEntity> findAllByUserId(String userId);
    void deleteAllByRoomId(String roomId);
}
