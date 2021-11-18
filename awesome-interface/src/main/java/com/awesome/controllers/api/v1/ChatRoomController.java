package com.awesome.controllers.api.v1;

import com.awesome.domains.chatroom.ChatRoomRepository;
import com.awesome.domains.chatroom.dtos.ChatRoomDTO;
import com.awesome.dtos.ChatRoomDetail;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/")
    public List<ChatRoomDTO> rooms(){
        return chatRoomRepository.findAllRooms();
    }

    @PostMapping("/")
    public void create(@RequestBody ChatRoomDetail chatRoomDetail){
        chatRoomDetail.setRoomCreatorUserId("abcd@abc.com");
        chatRoomDetail.setRoomMaxUserNum(30L);
        chatRoomRepository.save(chatRoomDetail.getRoomCreatorUserId(), chatRoomDetail.getRoomName(), chatRoomDetail.getRoomMaxUserNum());
    }
}