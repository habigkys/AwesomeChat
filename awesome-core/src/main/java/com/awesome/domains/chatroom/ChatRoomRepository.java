package com.awesome.domains.chatroom;

import com.awesome.domains.chatroom.entities.ChatRoomDAO;
import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import com.awesome.domains.chatroom.entities.ChatRoomMessageDAO;
import com.awesome.domains.chatroom.entities.ChatRoomMessageEntity;
import com.awesome.domains.chatroom.entities.specs.ChatRoomEntitySpec;
import com.awesome.domains.chatroom.entities.specs.ChatRoomMessageEntitySpec;
import com.awesome.domains.chatroom.entities.vos.ChatRoomPageInfoVO;
import com.awesome.infrastructures.page.PageListResponse;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class ChatRoomRepository {
  private final ChatRoomDAO chatRoomDAO;
  private final ChatRoomMessageDAO chatRoomMessageDAO;


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
}
