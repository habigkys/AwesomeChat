package com.awesome.controllers.api.v1;

import com.awesome.domains.chatroom.ChatRoom;
import com.awesome.domains.chatroom.ChatRoomRepository;
import com.awesome.domains.chatroom.ChatRoomService;
import com.awesome.dtos.ChatRoomDetail;
import com.awesome.infrastructures.shared.chatroom.ChatRoomDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;

    @GetMapping("/")
    public List<ChatRoomDTO> rooms(){
        List<ChatRoom> rooms = chatRoomRepository.findAll();
        return rooms.stream().map(e -> ChatRoomDTO.convertEntityToDto(e.getChatRoomEntity())).collect(Collectors.toList());
    }

    @PostMapping("/")
    public void create(@RequestBody ChatRoomDetail chatRoomDetail){
        ChatRoom newChatRoom = chatRoomService.create(chatRoomDetail.getRoomCreatorUserId(),
            chatRoomDetail.getRoomName(), chatRoomDetail.getRoomMaxUserNum());
        chatRoomRepository.save(newChatRoom);
    }
}