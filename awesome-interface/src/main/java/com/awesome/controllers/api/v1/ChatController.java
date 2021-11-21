package com.awesome.controllers.api.v1;

import com.awesome.domains.chatroom.ChatRoom;
import com.awesome.domains.chatroom.ChatRoomRepository;
import com.awesome.domains.chatroom.ChatRoomService;
import com.awesome.infrastructures.shared.chatroom.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate template;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDTO message){
        ChatRoom chatRoom = chatRoomService.enterRoom(message);
        chatRoomRepository.save(chatRoom);
        template.convertAndSend("/sub/chat/room/" + chatRoom.getChatRoomEntity().getRoomId(), ChatMessageDTO.convertEntityToDto(chatRoom.getChatRoomMessageEntities().get(0)));
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message){
        ChatRoom chatRoom = chatRoomService.sendMessage(message);
        chatRoomRepository.save(chatRoom);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/disconnect")
    public void disconnect(ChatMessageDTO message){
        ChatRoom chatRoom = chatRoomService.disconnectRoom(message);
        chatRoomRepository.save(chatRoom);
        template.convertAndSend("/sub/chat/room/" + chatRoom.getChatRoomEntity().getRoomId(), ChatMessageDTO.convertEntityToDto(chatRoom.getChatRoomMessageEntities().get(0)));
    }
}