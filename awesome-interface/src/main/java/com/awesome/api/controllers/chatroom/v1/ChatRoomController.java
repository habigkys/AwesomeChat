package com.awesome.api.controllers.chatroom.v1;

import com.awesome.domains.chatroom.ChatRoom;
import com.awesome.domains.chatroom.ChatRoomRepository;
import com.awesome.domains.chatroom.ChatRoomService;
import com.awesome.dtos.ChatRoomDetail;
import com.awesome.infrastructures.shared.chatroom.ChatDetailDTO;
import com.awesome.infrastructures.shared.chatroom.ChatMessageDetailDTO;
import com.awesome.infrastructures.shared.chatroom.ChatRoomDTO;
import com.wishwingz.platform.core.auth.simple.model.DefaultServiceUser;
import com.wishwingz.platform.service.auth.AuthDefaultClient;
import com.wishwingz.platform.service.auth.models.commands.LoginByCookiesAuthCommand;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final DefaultServiceUser defaultServiceUser;
    private final AuthDefaultClient authDefaultClient;

    @GetMapping
    public ChatDetailDTO rooms(
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "size", required = false, defaultValue = "15") int size
    ){
        ChatDetailDTO chatDetailDTO = chatRoomService.getLimitedSizeChatRooms(lastId, size);

        return chatDetailDTO;
    }

    @GetMapping("/{roomId}")
    public ChatRoomDTO room(@PathVariable Long roomId){
        ChatRoom room = chatRoomRepository.findChatRoomById(roomId);
        return ChatRoomDTO.convertEntityToDto(room.getChatRoomEntity());
    }

    @GetMapping("/room/joins")
    public boolean alreadyJoined(@RequestParam(value = "roomId") Long roomId){
        return chatRoomService.checkAlreadyJoined(roomId, defaultServiceUser.getUuid());
    }

    @GetMapping("/messages")
    public ChatMessageDetailDTO messages(
            @RequestParam(value = "roomId") Long roomId,
            @RequestParam(value = "lastId", required = false) Long lastId
    ){
        ChatMessageDetailDTO chatMessageDetailDTO = chatRoomService.getLimitedSizeChatMessages(roomId, lastId);

        return chatMessageDetailDTO;
    }

    @GetMapping("/owns")
    public ChatDetailDTO roomOwned(
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "size", required = false, defaultValue = "15") int size
    ){
        String userId = defaultServiceUser.getUuid();
        ChatDetailDTO chatDetailDTO = chatRoomService.getLimitedSizeOwnsChatRooms(lastId, size, userId);

        return chatDetailDTO;
    }

    @GetMapping("/joins")
    public List<ChatRoomDTO> roomJoined(){
        String userId = defaultServiceUser.getUuid();
        List<ChatRoom> rooms = chatRoomRepository.findRoomByJoinUserId(userId);
        return rooms.stream().map(e -> ChatRoomDTO.convertEntityToDto(e.getChatRoomEntity())).collect(Collectors.toList());
    }

    @PostMapping
    public ChatRoomDTO create(@RequestBody ChatRoomDetail chatRoomDetail){
        ChatRoom newChatRoom = chatRoomService.create(defaultServiceUser.getUuid(), defaultServiceUser.getName(), chatRoomDetail.getRoomName(), chatRoomDetail.getRoomMaxUserNum());
        chatRoomRepository.save(newChatRoom);
        chatRoomService.enterChatRoomInitCreater(newChatRoom);

        return ChatRoomDTO.convertEntityToDto(newChatRoom.getChatRoomEntity());
    }

    @DeleteMapping("/{roomId}")
    public void delete(@PathVariable Long roomId){
        ChatRoom room = chatRoomRepository.findChatRoomById(roomId);
        chatRoomRepository.remove(room);
    }

    @GetMapping("/cookietest1")
    public ResponseEntity<Boolean> cookietest1(HttpServletResponse res) throws Exception {
        LoginByCookiesAuthCommand cookiesAuthCommand = new LoginByCookiesAuthCommand();
        cookiesAuthCommand.setEmail("hanblueblue@naver.com").setName("손예린_robin").setUuid("465dc9b9-8829-41ab-86da-9bc5ff2e13e4");

        authDefaultClient.doInvokeCommand(cookiesAuthCommand.setResponse(res));

        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("/cookietest2")
    public ResponseEntity<Boolean> cookietest2(HttpServletResponse res) throws Exception {
        LoginByCookiesAuthCommand cookiesAuthCommand = new LoginByCookiesAuthCommand();
        cookiesAuthCommand
                .setEmail("kyww1119@gmail.com")
                .setName("이승만-william")
                .setUuid("5743c282-c534-4e1e-af39-caa16e3f78c7");

        authDefaultClient.doInvokeCommand(cookiesAuthCommand.setResponse(res));

        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("/mycookie")
    public String myCookie(){
        String userId = defaultServiceUser.getUuid();
        String userName = defaultServiceUser.getName();

        return "userId : " + userId + " userName : " + userName;
    }
}