package com.awesome.infrastructures.shared.chatroom;

import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomDTO {
    private Long id;
    private String roomCreatorUserId;
    private String roomCreatorUserName;
    private String roomName;
    private Long roomMaxUserNum;
    private Long roomCurUserNum;
    private LocalDateTime regDateTime;
    private LocalDateTime updDateTime;

    public static ChatRoomDTO convertEntityToDto(ChatRoomEntity chatRoomEntity) {
        ChatRoomDTO dto = new ChatRoomDTO();
        dto.setId(chatRoomEntity.getId());
        dto.setRoomCreatorUserId(chatRoomEntity.getRoomCreatorUserId());
        dto.setRoomCreatorUserName(chatRoomEntity.getRoomCreatorUserName());
        dto.setRoomName(chatRoomEntity.getRoomName());
        dto.setRoomMaxUserNum(chatRoomEntity.getRoomMaxUserNum());
        dto.setRoomCurUserNum(chatRoomEntity.getRoomCurUserNum());
        dto.setRegDateTime(chatRoomEntity.getRegDateTime());
        dto.setUpdDateTime(chatRoomEntity.getUpdDateTime());
        return dto;
    }

    public static ChatRoomEntity convertDtoToEntity(ChatRoomDTO chatRoomDTO) {
        ChatRoomEntity entity = new ChatRoomEntity();
        entity.setId(chatRoomDTO.getId());
        entity.setRoomCreatorUserId(chatRoomDTO.getRoomCreatorUserId());
        entity.setRoomCreatorUserName(chatRoomDTO.getRoomCreatorUserName());
        entity.setRoomName(chatRoomDTO.getRoomName());
        entity.setRoomMaxUserNum(chatRoomDTO.getRoomMaxUserNum());
        entity.setRoomCurUserNum(chatRoomDTO.getRoomCurUserNum());
        entity.setRegDateTime(chatRoomDTO.getRegDateTime());
        entity.setUpdDateTime(chatRoomDTO.getUpdDateTime());
        return entity;
    }
}
