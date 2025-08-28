package com.chat.ex.domain.chat.repository;

import com.chat.ex.domain.chat.entity.ChatMessage;
import com.chat.ex.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
