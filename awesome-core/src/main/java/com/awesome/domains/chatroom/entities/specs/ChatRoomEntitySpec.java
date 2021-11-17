package com.awesome.domains.chatroom.entities.specs;

import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import com.google.common.collect.Lists;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomEntitySpec {
    public static Specification<ChatRoomEntity> hasId(Long id) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("id"), id);
            }
        };
    }


    public static Specification<ChatRoomEntity> hasIds(List<Long> ids) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return root.get("id").in(ids);
            }
        };
    }

    public static Specification<ChatRoomEntity> hasMemberIds(List<Long> memberIds) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return root.get("memberId").in(memberIds);
            }
        };
    }

    public static Specification<ChatRoomEntity> hasMemberId(Long memberId) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("memberId"), memberId);
            }
        };
    }

    public static Specification<ChatRoomEntity> hasRoomId(String roomId) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("roomId"), roomId);
            }
        };
    }

    public static Specification<ChatRoomEntity> regDateTimeGreaterThan(LocalDateTime localDateTime) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("regDateTime"), localDateTime);
            }
        };
    }

    public static Specification<ChatRoomEntity> regDateTimeDesc() {
        return new Specification<ChatRoomEntity>() {
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Order> orders = Lists.newArrayList();
                orders.add(cb.desc(root.get("regDateTime")));
                query.orderBy(orders);
                return cb.conjunction();
            }
        };
    }
}
