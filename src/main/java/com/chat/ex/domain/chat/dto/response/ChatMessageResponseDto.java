package com.chat.ex.domain.chat.dto.response;

import com.chat.ex.domain.chat.entity.ChatRoom;
import com.chat.ex.domain.chat.entity.ChatUser;
import com.chat.ex.domain.chat.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageResponseDto {

    private Long id;
    private MessageType messageType;
    private Long roomId;
    private Long senderId;
    private String nickname;
    private String content;

}
