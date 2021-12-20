package com.awesome.infrastructures.shared.chatroom;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatDetailDTO {
    private boolean next;
    private List<ChatRoomDTO> rooms;

    static public ChatDetailDTO empty() {
        ChatDetailDTO dto = new ChatDetailDTO();
        dto.setNext(false);
        dto.setRooms(Lists.newArrayList());

        return dto;
    }
}
