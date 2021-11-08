package com.awesome.domains.chat.dtos;

import com.awesome.domains.chat.entities.ChatRoomEntity;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatRoomDTO {
    private Long id;
    private String roomId;
    private String name;

    public static ChatRoomDTO convertEntityToDto(ChatRoomEntity chatRoomEntity) {
        ChatRoomDTO dto = new ChatRoomDTO();
        dto.setId(chatRoomEntity.getId());
        dto.setRoomId(chatRoomEntity.getRoomId());
        dto.setName(chatRoomEntity.getName());
        return dto;
    }

    public static ChatRoomEntity convertDtoToEntity(ChatRoomDTO chatRoomDTO) {
        ChatRoomEntity entity = new ChatRoomEntity();
        entity.setId(chatRoomDTO.getId());
        entity.setRoomId(chatRoomDTO.getRoomId());
        entity.setName(chatRoomDTO.getName());
        return entity;
    }
}
