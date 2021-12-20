package com.awesome.infrastructures.shared.chatroom;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatMessageDetailDTO {
    private boolean next;
    private List<ChatMessageDTO> messages;

    static public ChatMessageDetailDTO empty() {
        ChatMessageDetailDTO dto = new ChatMessageDetailDTO();
        dto.setNext(false);
        dto.setMessages(Lists.newArrayList());

        return dto;
    }
}
