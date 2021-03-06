package com.awesome.domains.chatroom.entities.specs;

import com.awesome.domains.chatroom.entities.ChatRoomMessageEntity;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomMessageEntitySpec {

    public static Specification<ChatRoomMessageEntity> hasId(Long id) {
        return new Specification<ChatRoomMessageEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomMessageEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("id"), id);
            }
        };
    }


    public static Specification<ChatRoomMessageEntity> hasIds(List<Long> ids) {
        return new Specification<ChatRoomMessageEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomMessageEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return root.get("id").in(ids);
            }
        };
    }

    public static Specification<ChatRoomMessageEntity> hasMessageSendUserIds(List<String> messageSendUserIds) {
        return new Specification<ChatRoomMessageEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomMessageEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return root.get("messageSendUserId").in(messageSendUserIds);
            }
        };
    }

    public static Specification<ChatRoomMessageEntity> hasMessageSendUserId(String messageSendUserId) {
        return new Specification<ChatRoomMessageEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomMessageEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("messageSendUserId"), messageSendUserId);
            }
        };
    }

    public static Specification<ChatRoomMessageEntity> hasRoomId(Long roomId) {
        return new Specification<ChatRoomMessageEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomMessageEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("roomId"), roomId);
            }
        };
    }

    public static Specification<ChatRoomMessageEntity> regDateTimeGreaterThan(LocalDateTime localDateTime) {
        return new Specification<ChatRoomMessageEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomMessageEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("regDateTime"), localDateTime);
            }
        };
    }

    public static Specification<ChatRoomMessageEntity> regDateTimeDesc() {
        return new Specification<ChatRoomMessageEntity>() {
            public Predicate toPredicate(Root<ChatRoomMessageEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Order> orders = Lists.newArrayList();
                orders.add(cb.desc(root.get("regDateTime")));
                query.orderBy(orders);
                return cb.conjunction();
            }
        };
    }
}
