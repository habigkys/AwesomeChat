package com.awesome.domains.chatroom;

import com.awesome.domains.chatroom.entities.*;
import com.awesome.domains.chatroom.entities.specs.ChatRoomEntitySpec;
import com.awesome.domains.chatroom.entities.specs.ChatRoomMessageEntitySpec;
import com.awesome.domains.chatroom.entities.specs.ChatRoomUserEntitySpec;
import com.awesome.domains.chatroom.entities.vos.ChatRoomPageInfoVO;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@AllArgsConstructor
@Repository
public class ChatRoomRepository {
    private final ChatRoomDAO chatRoomDAO;
    private final ChatRoomMessageDAO chatRoomMessageDAO;
    private final ChatRoomUserDAO chatRoomUserDAO;


    @Transactional
    public void save(ChatRoom chatRoom) {
        if (Objects.isNull(chatRoom)) {
            return;
        }

        if (Objects.nonNull(chatRoom.getChatRoomEntity())) {
            chatRoomDAO.save(chatRoom.getChatRoomEntity());
        }

        if (CollectionUtils.isNotEmpty(chatRoom.getChatRoomMessageEntities())) {
            chatRoomMessageDAO.saveAll(chatRoom.getChatRoomMessageEntities());
        }
    }


    @Transactional
    public void remove(ChatRoom chatRoom) {
        if (Objects.isNull(chatRoom)) {
            return;
        }

        if (Objects.nonNull(chatRoom.getChatRoomEntity())) {
            chatRoomDAO.delete(chatRoom.getChatRoomEntity());
        }

        if (CollectionUtils.isNotEmpty(chatRoom.getChatRoomMessageEntities())) {
            chatRoomMessageDAO.deleteAll(chatRoom.getChatRoomMessageEntities());
        }
    }

    public ChatRoom findById(Long id) {
        ChatRoom chatRoom = new ChatRoom();
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasId(id));

        if (byId.isEmpty()) {
            return chatRoom;
        }
        chatRoom.setChatRoomEntity(byId.get());
        List<ChatRoomMessageEntity> roomMessageEntities = chatRoomMessageDAO.findAll(ChatRoomMessageEntitySpec.hasRoomId(id));
        chatRoom.setChatRoomMessageEntities(roomMessageEntities);
        List<ChatRoomUserEntity> roomUserEntities = chatRoomUserDAO.findAll(ChatRoomUserEntitySpec.hasRoomId(id));
        chatRoom.setChatRoomUserEntities(roomUserEntities);
        return chatRoom;
    }

    public List<ChatRoom> findByRoomCreatorUserId(String userId) {
        List<ChatRoom> chatRooms = new ArrayList<>();
        List<ChatRoomEntity> byUserId = chatRoomDAO.findAll(ChatRoomEntitySpec.hasRoomCreatorUserId(userId));

        if (CollectionUtils.isEmpty(byUserId)) {
            return chatRooms;
        }

        for(ChatRoomEntity entity : byUserId){
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setChatRoomEntity(entity);
            chatRooms.add(chatRoom);
        }

        return chatRooms;
    }

    public List<ChatRoom> findRoomByJoinUserId(String userId) {
        List<ChatRoom> chatRooms = new ArrayList<>();
        List<ChatRoomUserEntity> roomUserEntities = chatRoomUserDAO.findAll(ChatRoomUserEntitySpec.hasUserId(userId));

        if (CollectionUtils.isEmpty(roomUserEntities)) {
            return chatRooms;
        }

        List<Long> roomIds = new ArrayList<>();

        for(ChatRoomUserEntity roomUserEntitiy : roomUserEntities){
            roomIds.add(roomUserEntitiy.getRoomId());
        }

        List<ChatRoomEntity> byRoomId = chatRoomDAO.findAll(ChatRoomEntitySpec.hasIds(roomIds));

        if (CollectionUtils.isEmpty(byRoomId)) {
            return chatRooms;
        }

        for(ChatRoomEntity entity : byRoomId){
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setChatRoomEntity(entity);
            chatRooms.add(chatRoom);
        }

        return chatRooms;
    }

    public ChatRoom findAllMessage(Long roomId, Pageable pageable) {
        ChatRoom chatRoom = new ChatRoom();
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasId(roomId));

        if (byId.isEmpty()) {
            return chatRoom;
        }
        chatRoom.setChatRoomEntity(byId.get());
        Page<ChatRoomMessageEntity> all = chatRoomMessageDAO.findAll(ChatRoomMessageEntitySpec.hasRoomId(roomId), pageable);
        List<ChatRoomMessageEntity> content = all.getContent();
        chatRoom.setChatRoomMessageEntities(content);
        return chatRoom;
    }

    //(0, 10)
    //Index 기반조회
    public ChatRoom findAllMessagePageable(Long roomId, Pageable pageable) {
        ChatRoom chatRoom = new ChatRoom();
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasId(roomId));

        if (byId.isEmpty()) {
            return chatRoom;
        }
        chatRoom.setChatRoomEntity(byId.get());
        Page<ChatRoomMessageEntity> all = chatRoomMessageDAO.findAll(ChatRoomMessageEntitySpec.hasRoomId(roomId)
                        .and(ChatRoomMessageEntitySpec.regDateTimeDesc())
                , pageable);

        List<ChatRoomMessageEntity> content = all.getContent();
        chatRoom.setChatRoomMessageEntities(content);

        ChatRoomPageInfoVO vo = new ChatRoomPageInfoVO();
        vo.setPage(all.getNumber());
        vo.setNext(all.hasNext());
        vo.setPrev(all.hasPrevious());
        vo.setTotalCount(all.getTotalElements());
        vo.setTotalPageSize(all.getTotalPages());
        chatRoom.setChatRoomMessagePageInfoVO(vo);
        return chatRoom;
    }

    public List<ChatRoom> findAll() {
        return null;
    }
}
