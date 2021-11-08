package com.awesome.domains.chat.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {
    private String roomId;
    private String user;
    private String message;
}
