package com.awesome.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDetail {
    private String roomId;
    private String roomName;
    private String roomCreatorUserId;
    private Long roomMaxUserNum;
    private Long roomCurUserNum;
}
