package com.awesome.infrastructures.shared.chatroom;

import com.awesome.domains.chatroom.entities.ChatRoomMessageEntity;
import com.awesome.domains.chatroom.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDTO {
    private Long id;
    private Long roomId;
    private String uuid;
    private MessageType messageType;
    private String messageSendUserId;
    private String messageSendUserName;
    private String message;
    private LocalDateTime regDateTime;

    public static ChatMessageDTO convertEntityToDto(ChatRoomMessageEntity chatRoomMessageEntity) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(chatRoomMessageEntity.getId());
        dto.setRoomId(chatRoomMessageEntity.getRoomId());
        dto.setMessageType(chatRoomMessageEntity.getMessageType());
        dto.setMessageSendUserId(chatRoomMessageEntity.getMessageSendUserId());
        dto.setMessageSendUserName(chatRoomMessageEntity.getMessageSendUserName());
        dto.setMessage(chatRoomMessageEntity.getMessage());
        dto.setRegDateTime(chatRoomMessageEntity.getRegDateTime());
        return dto;
    }

    public static ChatRoomMessageEntity convertDtoToEntity(ChatMessageDTO chatMessageDTO) {
        ChatRoomMessageEntity entity = new ChatRoomMessageEntity();
        entity.setId(chatMessageDTO.getId());
        entity.setRoomId(chatMessageDTO.getRoomId());
        entity.setMessageType(chatMessageDTO.getMessageType());
        entity.setMessageSendUserId(chatMessageDTO.getMessageSendUserId());
        entity.setMessageSendUserName(chatMessageDTO.getMessageSendUserName());
        entity.setMessage(chatMessageDTO.getMessage());
        entity.setRegDateTime(chatMessageDTO.getRegDateTime());
        return entity;
    }
}
