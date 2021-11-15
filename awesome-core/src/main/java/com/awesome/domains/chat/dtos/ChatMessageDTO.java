package com.awesome.domains.chat.dtos;

import com.awesome.domains.chat.entities.ChatMessageEntity;
import com.awesome.domains.chat.entities.ChatRoomEntity;
import com.awesome.domains.chat.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDTO {
    private Long id;
    private String roomId;
    private MessageType messageType;
    private String messageSendUserId;
    private String message;

    public static ChatMessageDTO convertEntityToDto(ChatMessageEntity chatMessageEntity) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(chatMessageEntity.getId());
        dto.setRoomId(chatMessageEntity.getRoomId());
        dto.setMessageType(chatMessageEntity.getMessageType());
        dto.setMessageSendUserId(chatMessageEntity.getMessageSendUserId());
        dto.setMessage(chatMessageEntity.getMessage());
        return dto;
    }

    public static ChatMessageEntity convertDtoToEntity(ChatMessageDTO chatMessageDTO) {
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setId(chatMessageDTO.getId());
        entity.setRoomId(chatMessageDTO.getRoomId());
        entity.setMessageType(chatMessageDTO.getMessageType());
        entity.setMessageSendUserId(chatMessageDTO.getMessageSendUserId());
        entity.setMessage(chatMessageDTO.getMessage());
        return entity;
    }
}
