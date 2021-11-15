package com.awesome.domains.chat.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageDAO extends JpaRepository<ChatMessageEntity, Long> {
}
