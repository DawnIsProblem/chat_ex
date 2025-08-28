package com.chat.ex.domain.chat.entity;

import com.chat.ex.domain.chat.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "chat_message",
        indexes = {
                @Index(name = "idx_msg_room_time", columnList = "room_id, created_at")
        }
)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_user_id", nullable = false)
    private ChatUser sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreated() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.nickname == null && this.sender != null) {
            this.nickname = this.sender.getNickname();
        }
    }

}
