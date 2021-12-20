package com.awesome.domains.chatroom;

import com.awesome.domains.chatroom.entities.*;
import com.awesome.domains.chatroom.entities.specs.ChatRoomEntitySpec;
import com.awesome.domains.chatroom.entities.specs.ChatRoomMessageEntitySpec;
import com.awesome.domains.chatroom.entities.specs.ChatRoomUserEntitySpec;
import com.awesome.domains.chatroom.entities.vos.ChatRoomPageInfoVO;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

        if (CollectionUtils.isNotEmpty(chatRoom.getChatRoomUserEntities())) {
            chatRoomUserDAO.saveAll(chatRoom.getChatRoomUserEntities());
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

        if (CollectionUtils.isNotEmpty(chatRoom.getChatRoomUserEntities())) {
            chatRoomUserDAO.deleteAll(chatRoom.getChatRoomUserEntities());
        }
    }

    @Transactional
    public void removeUser(ChatRoom chatRoom) {
        if (Objects.isNull(chatRoom)) {
            return;
        }

        if (CollectionUtils.isNotEmpty(chatRoom.getChatRoomUserEntities())) {
            chatRoomUserDAO.deleteAll(chatRoom.getChatRoomUserEntities());
        }
    }

    public ChatRoom findChatRoomById(Long id) {
        ChatRoom chatRoom = new ChatRoom();
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasId(id));

        if (byId.isEmpty()) {
            return chatRoom;
        }

        chatRoom.setChatRoomEntity(byId.get());

        List<ChatRoomUserEntity> chatRoomUserEntities = chatRoomUserDAO.findAll(ChatRoomUserEntitySpec.hasRoomId(id));

        if (CollectionUtils.isNotEmpty(chatRoomUserEntities)) {
            chatRoom.setChatRoomUserEntities(chatRoomUserEntities);
        }

        return chatRoom;
    }

    public ChatRoom findChatRoomByIdAndUserId(Long id, String userId) {
        ChatRoom chatRoom = new ChatRoom();
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasId(id));

        if (byId.isEmpty()) {
            return chatRoom;
        }

        chatRoom.setChatRoomEntity(byId.get());

        List<ChatRoomUserEntity> chatRoomUserEntities = chatRoomUserDAO.findAll(ChatRoomUserEntitySpec.hasRoomId(id)
                .and(ChatRoomUserEntitySpec.hasUserId(userId)));

        if (CollectionUtils.isNotEmpty(chatRoomUserEntities)) {
            chatRoom.setChatRoomUserEntities(chatRoomUserEntities);
        }

        return chatRoom;
    }

    public List<ChatRoom> findByRoomCreatorUserId(String userId) {
        List<ChatRoom> chatRooms = new ArrayList<>();
        List<ChatRoomEntity> byUserId = chatRoomDAO.findAll(ChatRoomEntitySpec.hasRoomCreatorUserId(userId));

        if (CollectionUtils.isEmpty(byUserId)) {
            return chatRooms;
        }

        for (ChatRoomEntity entity : byUserId) {
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

        for (ChatRoomUserEntity roomUserEntitiy : roomUserEntities) {
            roomIds.add(roomUserEntitiy.getRoomId());
        }

        List<ChatRoomEntity> byRoomId = chatRoomDAO.findAll(ChatRoomEntitySpec.hasIds(roomIds));

        if (CollectionUtils.isEmpty(byRoomId)) {
            return chatRooms;
        }

        for (ChatRoomEntity entity : byRoomId) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setChatRoomEntity(entity);
            chatRooms.add(chatRoom);
        }

        return chatRooms;
    }

    public ChatRoom findAllMessage(Long roomId) {
        ChatRoom chatRoom = new ChatRoom();
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasId(roomId));

        if (byId.isEmpty()) {
            return chatRoom;
        }
        chatRoom.setChatRoomEntity(byId.get());
        List<ChatRoomMessageEntity> all = chatRoomMessageDAO.findAll(ChatRoomMessageEntitySpec.hasRoomId(roomId));
        chatRoom.setChatRoomMessageEntities(all);
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

    public List<ChatRoom> findAllChatRoom() {
        List<ChatRoom> chatRooms = new ArrayList<>();

        List<ChatRoomEntity> chatRoomEntities = chatRoomDAO.findAll();

        for (ChatRoomEntity entity : chatRoomEntities) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setChatRoomEntity(entity);
            chatRooms.add(chatRoom);
        }

        return chatRooms;
    }

    public Page<ChatRoom> findAllChatRoomPageable(Long id, Pageable limit) {
        Page<ChatRoomEntity> chatRoomEntities = chatRoomDAO.findAll(ChatRoomEntitySpec.lessThanIdOrderByIdDesc(id), limit);
        List<ChatRoom> chatRooms = chatRoomEntities.stream()
                .map(chatRoomEntity -> {
                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.setChatRoomEntity(chatRoomEntity);
                    return chatRoom;
                })
                .collect(Collectors.toList());

        return new PageImpl<ChatRoom>(chatRooms, limit, chatRoomEntities.getTotalElements());
    }

    public Page<ChatRoom> findInitAllChatRoom(Pageable limit) {
        Page<ChatRoomEntity> chatRoomEntities = chatRoomDAO.findAll(ChatRoomEntitySpec.orderByIdDesc(), limit);
        List<ChatRoom> chatRooms = chatRoomEntities.stream()
                .map(chatRoomEntity -> {
                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.setChatRoomEntity(chatRoomEntity);
                    return chatRoom;
                })
                .collect(Collectors.toList());

        return new PageImpl<ChatRoom>(chatRooms, limit, chatRoomEntities.getTotalElements());
    }

    public Page<ChatRoom> findAllOwnsChatRoomPageable(Long id, String userId, Pageable limit) {
        Page<ChatRoomEntity> chatRoomEntities = chatRoomDAO.findAll(ChatRoomEntitySpec.hasRoomCreatorUserIdAndLessThanIdOrderByIdDesc(userId, id), limit);

        List<ChatRoom> chatRooms = chatRoomEntities.stream()
                .map(chatRoomEntity -> {
                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.setChatRoomEntity(chatRoomEntity);
                    return chatRoom;
                })
                .collect(Collectors.toList());

        return new PageImpl<ChatRoom>(chatRooms, limit, chatRoomEntities.getTotalElements());
    }

    public Page<ChatRoom> findInitChatOwnedRoom(String userId, Pageable limit) {
        Page<ChatRoomEntity> chatRoomEntities = chatRoomDAO.findAll(ChatRoomEntitySpec.hasRoomCreatorUserIdOrderByIdDesc(userId), limit);
        List<ChatRoom> chatRooms = chatRoomEntities.stream()
                .map(chatRoomEntity -> {
                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.setChatRoomEntity(chatRoomEntity);
                    return chatRoom;
                })
                .collect(Collectors.toList());

        return new PageImpl<ChatRoom>(chatRooms, limit, chatRoomEntities.getTotalElements());
    }

    public void appendEnterChatRoomMessage(ChatRoom chatRoom, Pageable limit) {
        Page<ChatRoomMessageEntity> all = chatRoomMessageDAO.findAll(ChatRoomMessageEntitySpec.hasRoomIdOrderByIdDesc(chatRoom.getChatRoomEntity().getId()), limit);
        List<ChatRoomMessageEntity> messageEntities = all.getContent();

        List<ChatRoomMessageEntity> orgEntities = chatRoom.getChatRoomMessageEntities();
        orgEntities.addAll(messageEntities);
        chatRoom.setChatRoomMessageEntities(orgEntities);
    }

    public ChatRoom findChatRoomMessagePageable(Long roomId, Long id, Pageable limit) {
        ChatRoom chatRoom = new ChatRoom();
        ChatRoomPageInfoVO chatRoomPageInfoVO = new ChatRoomPageInfoVO();
        chatRoom.setChatRoomMessagePageInfoVO(chatRoomPageInfoVO);
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasId(roomId));

        if (byId.isEmpty()) {
            return chatRoom;
        }

        Page<ChatRoomMessageEntity> chatRoomMessageEntities;

        if(id == null){
            chatRoomMessageEntities = chatRoomMessageDAO.findAll(ChatRoomMessageEntitySpec.hasRoomIdOrderByIdDesc(roomId), limit);
        }else{
            chatRoomMessageEntities = chatRoomMessageDAO.findAll(ChatRoomMessageEntitySpec.hasRoomIdAndLessThanIdOrderByIdDesc(roomId, id), limit);
        }

        if(chatRoomMessageEntities.isEmpty()){
            return chatRoom;
        }

        chatRoom.setChatRoomMessageEntities(Lists.newArrayList(new LinkedList<>(chatRoomMessageEntities.getContent()).descendingIterator()));

        chatRoomPageInfoVO.setTotalPageSize(chatRoomMessageEntities.getTotalPages());
        chatRoomPageInfoVO.setTotalCount(chatRoomMessageEntities.getTotalElements());
        chatRoomPageInfoVO.setNext(chatRoomMessageEntities.hasNext());
        chatRoomPageInfoVO.setPrev(chatRoomMessageEntities.hasPrevious());
        chatRoomPageInfoVO.setPage(chatRoomMessageEntities.getNumber());
        chatRoomPageInfoVO.setPageSize(chatRoomMessageEntities.getSize());

        chatRoom.setChatRoomMessagePageInfoVO(chatRoomPageInfoVO);

        return chatRoom;
    }
}
