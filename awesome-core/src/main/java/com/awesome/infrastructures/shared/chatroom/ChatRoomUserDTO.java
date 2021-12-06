package com.awesome.infrastructures.shared.chatroom;

import com.awesome.domains.chatroom.entities.ChatRoomUserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomUserDTO {
    private Long id;
    private Long roomId;
    private String userId;
    private String userName;
    private LocalDateTime regDateTime;

    public static ChatRoomUserDTO convertEntityToDto(ChatRoomUserEntity chatRoomUserEntity) {
        ChatRoomUserDTO dto = new ChatRoomUserDTO();
        dto.setId(chatRoomUserEntity.getId());
        dto.setRoomId(chatRoomUserEntity.getRoomId());
        dto.setUserId(chatRoomUserEntity.getUserId());
        dto.setUserName(chatRoomUserEntity.getUserName());
        dto.setRegDateTime(chatRoomUserEntity.getRegDateTime());
        return dto;
    }

    public static ChatRoomUserEntity convertDtoToEntity(ChatRoomUserDTO chatRoomUserDTO) {
        ChatRoomUserEntity entity = new ChatRoomUserEntity();
        entity.setId(chatRoomUserDTO.getId());
        entity.setRoomId(chatRoomUserDTO.getRoomId());
        entity.setUserId(chatRoomUserDTO.getUserId());
        entity.setUserName(chatRoomUserDTO.getUserName());
        entity.setRegDateTime(chatRoomUserDTO.getRegDateTime());
        return entity;
    }
}
