package com.awesome.infrastructures.shared.chatroom;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatDetailDTO {
    private String next;
    private List<ChatRoomDTO> rooms;
}
