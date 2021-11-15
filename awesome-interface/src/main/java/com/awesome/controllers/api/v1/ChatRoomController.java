package com.awesome.controllers.api.v1;

import com.awesome.domains.chat.dtos.ChatRoomDTO;
import com.awesome.domains.chat.services.ChatService;
import com.awesome.dtos.ChatRoomDetail;
import com.awesome.dtos.ProjectDetail;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/chat")
public class ChatRoomController {
    private final ChatService chatService;

    @GetMapping("/rooms")
    public List<ChatRoomDTO> rooms(){
        return chatService.findAllRooms();
    }

    @PostMapping("/room")
    public ChatRoomDTO create(@RequestBody ChatRoomDetail chatRoomDetail){
        chatRoomDetail.setRoomCreatorUserId("abcd@abc.com");
        chatRoomDetail.setRoomMaxUserNum(30L);
        return chatService.createChatRoom(chatRoomDetail.getRoomCreatorUserId(), chatRoomDetail.getRoomName(), chatRoomDetail.getRoomMaxUserNum());
    }

    @GetMapping("/room/{roomId}")
    public ChatRoomDTO getRoom(@PathVariable("roomId") String roomId){
        return chatService.findRoomById(roomId);
    }
}