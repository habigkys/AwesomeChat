package com.awesome.controllers.api.v1;

import com.awesome.domains.chatroom.ChatRoom;
import com.awesome.domains.chatroom.ChatRoomRepository;
import com.awesome.domains.chatroom.ChatRoomService;
import com.awesome.dtos.ChatRoomDetail;
import com.awesome.infrastructures.shared.chatroom.ChatDetailDTO;
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

    @GetMapping
    public ChatDetailDTO rooms(@RequestParam("id") Long id, @RequestParam("size") int size){
        ChatDetailDTO chatDetailDTO = chatRoomService.getLimitedSizeChatRooms(id, size);

        return chatDetailDTO;
    }

    @GetMapping("/{roomId}")
    public ChatRoomDTO room(@PathVariable Long roomId){
        ChatRoom room = chatRoomRepository.findChatRoomById(roomId);
        return ChatRoomDTO.convertEntityToDto(room.getChatRoomEntity());
    }

    @GetMapping("/owns")
    public ChatDetailDTO roomOwned(@RequestParam("id") Long id, @RequestParam("size") int size){
        String userId = "defaultServiceUser.getUuid()";
        ChatDetailDTO chatDetailDTO = chatRoomService.getLimitedSizeOwnsChatRooms(id, size, userId);

        return chatDetailDTO;
    }

    @GetMapping("/joins")
    public List<ChatRoomDTO> roomJoined(){
        String userId = "defaultServiceUser.getUuid()";
        List<ChatRoom> rooms = chatRoomRepository.findRoomByJoinUserId(userId);
        return rooms.stream().map(e -> ChatRoomDTO.convertEntityToDto(e.getChatRoomEntity())).collect(Collectors.toList());
    }

    @PostMapping
    public ChatRoomDTO create(@RequestBody ChatRoomDetail chatRoomDetail){
        ChatRoom newChatRoom = chatRoomService.create("aabc", "aabc", chatRoomDetail.getRoomName(), 30L);
        chatRoomRepository.save(newChatRoom);

        return ChatRoomDTO.convertEntityToDto(newChatRoom.getChatRoomEntity());
    }

    @DeleteMapping("/{roomId}")
    public void delete(@PathVariable Long roomId){
        ChatRoom room = chatRoomRepository.findChatRoomById(roomId);
        chatRoomRepository.remove(room);
    }
}