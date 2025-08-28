package com.chat.ex.domain.chat.repository;

import com.chat.ex.domain.chat.entity.ChatRoom;
import com.chat.ex.domain.chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    boolean existsByRoomIdAndUserId(Long roomId, Long userId);
    Optional<ChatRoomMember> findByRoomIdAndUserId(Long roomId, Long userId);

}
