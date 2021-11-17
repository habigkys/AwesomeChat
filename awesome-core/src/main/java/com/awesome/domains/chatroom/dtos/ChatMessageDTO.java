package com.awesome.domains.chatroom.dtos;

import com.awesome.domains.chatroom.entities.ChatRoomMessageEntity;
import com.awesome.domains.chatroom.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {
    private Long id;
    private String roomId;
    private MessageType messageType;
    private String messageSendUserId;
    private String message;

    public static ChatMessageDTO convertEntityToDto(ChatRoomMessageEntity chatRoomMessageEntity) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(chatRoomMessageEntity.getId());
        dto.setRoomId(chatRoomMessageEntity.getRoomId());
        dto.setMessageType(chatRoomMessageEntity.getMessageType());
        dto.setMessageSendUserId(chatRoomMessageEntity.getMessageSendUserId());
        dto.setMessage(chatRoomMessageEntity.getMessage());
        return dto;
    }

    public static ChatRoomMessageEntity convertDtoToEntity(ChatMessageDTO chatMessageDTO) {
        ChatRoomMessageEntity entity = new ChatRoomMessageEntity();
        entity.setId(chatMessageDTO.getId());
        entity.setRoomId(chatMessageDTO.getRoomId());
        entity.setMessageType(chatMessageDTO.getMessageType());
        entity.setMessageSendUserId(chatMessageDTO.getMessageSendUserId());
        entity.setMessage(chatMessageDTO.getMessage());
        return entity;
    }
}
