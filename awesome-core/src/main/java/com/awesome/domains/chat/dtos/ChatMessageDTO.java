package com.awesome.domains.chat.dtos;

import com.awesome.domains.chat.enums.MessageType;
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
}
