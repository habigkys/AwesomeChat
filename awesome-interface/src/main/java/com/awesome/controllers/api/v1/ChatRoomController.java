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

    @GetMapping("/{roomId}")
    public ChatRoomDTO room(@PathVariable Long roomId){
        ChatRoom room = chatRoomRepository.findById(roomId);
        return ChatRoomDTO.convertEntityToDto(room.getChatRoomEntity());
    }

    @GetMapping("/owns")
    public List<ChatRoomDTO> roomOwned(){
        String userId = "123"; // 쿠키에서 UserId 가져오기 넣어야됨
        List<ChatRoom> rooms = chatRoomRepository.findByRoomCreatorUserId(userId);
        return rooms.stream().map(e -> ChatRoomDTO.convertEntityToDto(e.getChatRoomEntity())).collect(Collectors.toList());
    }

    @GetMapping("/joins")
    public List<ChatRoomDTO> roomJoined(){
        String userId = "123"; // 쿠키에서 UserId 가져오기 넣어야됨
        List<ChatRoom> rooms = chatRoomRepository.findRoomByJoinUserId(userId);
        return rooms.stream().map(e -> ChatRoomDTO.convertEntityToDto(e.getChatRoomEntity())).collect(Collectors.toList());
    }

    @PostMapping("/")
    public ChatRoomDTO create(@RequestBody ChatRoomDetail chatRoomDetail){
        ChatRoom newChatRoom = chatRoomService.create(chatRoomDetail.getRoomName(), chatRoomDetail.getRoomMaxUserNum());
        chatRoomRepository.save(newChatRoom);

        return ChatRoomDTO.convertEntityToDto(newChatRoom.getChatRoomEntity());
    }

    @DeleteMapping("/{roomId}")
    public void delete(@PathVariable Long roomId){
        ChatRoom room = chatRoomRepository.findById(roomId);
        chatRoomRepository.remove(room);
    }
}