package com.awesome.domains.chatroom;

import com.awesome.domains.chatroom.dtos.ChatMessageDTO;
import com.awesome.domains.chatroom.dtos.ChatRoomDTO;
import com.awesome.domains.chatroom.entities.*;
import com.awesome.domains.chatroom.entities.specs.ChatRoomEntitySpec;
import com.awesome.domains.chatroom.entities.specs.ChatRoomMessageEntitySpec;
import com.awesome.domains.chatroom.entities.specs.ChatRoomUserEntitySpec;
import com.awesome.domains.chatroom.entities.vos.ChatRoomPageInfoVO;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class ChatRoomRepository {
    private final ChatRoomDAO chatRoomDAO;
    private final ChatRoomMessageDAO chatRoomMessageDAO;
    private final ChatRoomUserDAO chatRoomUserDAO;

    public List<ChatRoomDTO> findAllRooms() {
        return chatRoomDAO.findAll().stream().map(ChatRoomDTO::convertEntityToDto).collect(Collectors.toList());
    }

    @Transactional
    public void save(String roomCreatorUserId, String name, Long roomMaxUserNum) {
        if (Objects.isNull(roomCreatorUserId)) {
            return;
        }

        if (Objects.isNull(name)) {
            return;
        }

        if (Objects.isNull(roomMaxUserNum)) {
            return;
        }

        ChatRoom chatRoom = new ChatRoom();
        ChatRoomEntity entity = new ChatRoomEntity();
        entity.setRoomId(uuidToBase64(UUID.randomUUID().toString()));
        entity.setRoomCreatorUserId(roomCreatorUserId);
        entity.setRoomName(name);
        entity.setRoomMaxUserNum(roomMaxUserNum);
        entity.setRoomCurUserNum(0L);
        chatRoom.setChatRoomEntity(entity);

        if (Objects.nonNull(chatRoom.getChatRoomEntity())) {
            chatRoomDAO.save(chatRoom.getChatRoomEntity());
        }

        if (CollectionUtils.isNotEmpty(chatRoom.getChatRoomMessageEntities())) {
            chatRoomMessageDAO.saveAll(chatRoom.getChatRoomMessageEntities());
        }
    }

    @Transactional
    public ChatRoom enterRoom(ChatMessageDTO chatMessageDTO){
        ChatRoom chatRoom = new ChatRoom();
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasRoomId(chatMessageDTO.getRoomId()));

        if (byId.isEmpty()) {
            return chatRoom;
        }

        chatRoom.setChatRoomEntity(byId.get());
        List<ChatRoomUserEntity> chatRoomUserEntities = chatRoomUserDAO.findAll(ChatRoomUserEntitySpec.hasUserId(chatMessageDTO.getMessageSendUserId()));

        if(CollectionUtils.isEmpty(chatRoomUserEntities)){
            ChatRoomUserEntity chatRoomUserEntity = new ChatRoomUserEntity();
            chatRoomUserEntity.setRoomId(byId.get().getRoomId());
            chatRoomUserEntity.setUserId(chatMessageDTO.getMessageSendUserId());

            chatRoomUserDAO.save(chatRoomUserEntity);

            ChatRoomEntity chatRoomEntity = byId.get();
            chatRoomEntity.setRoomCurUserNum(chatRoomEntity.getRoomCurUserNum()+1);
            chatRoom.setChatRoomEntity(chatRoomDAO.save(chatRoomEntity));

            chatMessageDTO.setMessage(chatMessageDTO.getMessageSendUserId() + "님이 채팅방에 참여하였습니다.");

            List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
            ChatRoomMessageEntity messageEntity = ChatMessageDTO.convertDtoToEntity(chatMessageDTO);
            messageEntities.add(messageEntity);

            chatRoom.setChatRoomMessageEntities(messageEntities);
        }

        return chatRoom;
    }

    @Transactional
    public void sendMessage(ChatMessageDTO chatMessageDTO){
        chatRoomMessageDAO.save(ChatMessageDTO.convertDtoToEntity(chatMessageDTO));
    }

    @Transactional
    public ChatRoom disconnectRoom(ChatMessageDTO chatMessageDTO){
        ChatRoom chatRoom = new ChatRoom();
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasRoomId(chatMessageDTO.getRoomId()));

        if (byId.isEmpty()) {
            return chatRoom;
        }

        chatRoom.setChatRoomEntity(byId.get());
        List<ChatRoomUserEntity> chatRoomUserEntities = chatRoomUserDAO.findAll(ChatRoomUserEntitySpec.hasRoomId(chatMessageDTO.getRoomId()));

        if(CollectionUtils.isNotEmpty(chatRoomUserEntities)) {
            ChatRoomUserEntity chatRoomUserEntity = new ChatRoomUserEntity();
            chatRoomUserEntity.setRoomId(chatRoomUserEntity.getRoomId());
            chatRoomUserEntity.setUserId(chatMessageDTO.getMessageSendUserId());

            chatRoomUserDAO.save(chatRoomUserEntity);

            ChatRoomEntity chatRoomEntity = byId.get();
            chatRoomEntity.setRoomCurUserNum(chatRoomEntity.getRoomCurUserNum() - 1);
            chatRoom.setChatRoomEntity(chatRoomDAO.save(chatRoomEntity));

            chatMessageDTO.setMessage(chatMessageDTO.getMessageSendUserId() + "님이 채팅방에서 나갔습니다.");

            List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
            ChatRoomMessageEntity messageEntity = ChatMessageDTO.convertDtoToEntity(chatMessageDTO);
            messageEntities.add(messageEntity);

            chatRoom.setChatRoomMessageEntities(messageEntities);
        }

        return chatRoom;
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

    public ChatRoom findById(String roomId) {
        ChatRoom chatRoom = new ChatRoom();
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasRoomId(roomId));

        if (byId.isEmpty()) {
            return chatRoom;
        }
        chatRoom.setChatRoomEntity(byId.get());
        List<ChatRoomMessageEntity> roomMessageEntities = chatRoomMessageDAO.findAll(ChatRoomMessageEntitySpec.hasRoomId(roomId));
        chatRoom.setChatRoomMessageEntities(roomMessageEntities);
        return chatRoom;
    }

    public ChatRoom findAllMessage(String roomId, Pageable pageable) {
        ChatRoom chatRoom = new ChatRoom();
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasRoomId(roomId));

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
    public ChatRoom findAllMessagePageable(String roomId, Pageable pageable) {
        ChatRoom chatRoom = new ChatRoom();
        Optional<ChatRoomEntity> byId = chatRoomDAO.findOne(ChatRoomEntitySpec.hasRoomId(roomId));

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

    private static String uuidToBase64(String str) {
        Base64 base64 = new Base64();
        UUID uuid = UUID.fromString(str);
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return base64.encodeBase64URLSafeString(bb.array());
    }
}
