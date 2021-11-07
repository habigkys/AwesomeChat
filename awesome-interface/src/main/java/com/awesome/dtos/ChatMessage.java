package com.awesome.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    public enum MessageType {
        ENTER, COMM
    }

    private MessageType messageType;
    private String roomId;
    private String roomName;
    private String user;
    private String message;
}
