package com.awesome.domains.chatroom.entities.specs;

import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import com.google.common.collect.Lists;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public static Specification<ChatRoomEntity> hasRoomCreatorUserIds(List<String> roomCreatorUserIds) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return root.get("roomCreatorUserId").in(roomCreatorUserIds);
            }
        };
    }

    public static Specification<ChatRoomEntity> hasRoomCreatorUserId(String roomCreatorUserId) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("roomCreatorUserId"), roomCreatorUserId);
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

    public static Specification<ChatRoomEntity> regDateTimeLessThan(LocalDateTime localDateTime) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("regDateTime"), localDateTime);
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

    public static Specification<ChatRoomEntity> lessThanIdOrderByIdDesc(Long id) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.orderBy(criteriaBuilder.desc(root.get("id")));
                return criteriaBuilder.lessThan(root.get("id"), id);
            }
        };
    }

    public static Specification<ChatRoomEntity> orderByIdDesc() {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.orderBy(criteriaBuilder.desc(root.get("id")));
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static Specification<ChatRoomEntity> hasRoomCreatorUserIdAndLessThanIdOrderByIdDesc(String roomCreatorUserId, Long lastRoomId) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.orderBy(criteriaBuilder.desc(root.get("id")));

                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("roomCreatorUserId"), roomCreatorUserId));
                predicates.add(criteriaBuilder.lessThan(root.get("id"), lastRoomId));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

    public static Specification<ChatRoomEntity> hasRoomCreatorUserIdOrderByIdDesc(String roomCreatorUserId) {
        return new Specification<ChatRoomEntity>() {
            @Override
            public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.orderBy(criteriaBuilder.desc(root.get("id")));
                return criteriaBuilder.equal(root.get("roomCreatorUserId"), roomCreatorUserId);
            }
        };
    }
}
