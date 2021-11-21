package com.awesome.domains.chatroom.entities.specs;

import com.awesome.domains.chatroom.entities.ChatRoomUserEntity;
import com.google.common.collect.Lists;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomUserEntitySpec {
    public static Specification<ChatRoomUserEntity> hasId(Long id) {
        return new Specification<ChatRoomUserEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("id"), id);
            }
        };
    }


    public static Specification<ChatRoomUserEntity> hasIds(List<Long> ids) {
        return new Specification<ChatRoomUserEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return root.get("id").in(ids);
            }
        };
    }

    public static Specification<ChatRoomUserEntity> hasUserIds(List<Long> userIds) {
        return new Specification<ChatRoomUserEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return root.get("userId").in(userIds);
            }
        };
    }

    public static Specification<ChatRoomUserEntity> hasUserId(String userId) {
        return new Specification<ChatRoomUserEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("userId"), userId);
            }
        };
    }

    public static Specification<ChatRoomUserEntity> hasRoomId(Long roomId) {
        return new Specification<ChatRoomUserEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("roomId"), roomId);
            }
        };
    }

    public static Specification<ChatRoomUserEntity> regDateTimeGreaterThan(LocalDateTime localDateTime) {
        return new Specification<ChatRoomUserEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("regDateTime"), localDateTime);
            }
        };
    }

    public static Specification<ChatRoomUserEntity> regDateTimeDesc() {
        return new Specification<ChatRoomUserEntity>() {
            public Predicate toPredicate(Root<ChatRoomUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Order> orders = Lists.newArrayList();
                orders.add(cb.desc(root.get("regDateTime")));
                query.orderBy(orders);
                return cb.conjunction();
            }
        };
    }
}
