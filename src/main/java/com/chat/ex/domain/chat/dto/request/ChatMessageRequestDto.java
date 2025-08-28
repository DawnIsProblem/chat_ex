package com.chat.ex.domain.chat.dto.request;

import com.chat.ex.domain.chat.enums.MessageType;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDto {

    private MessageType messageType;
    private Long roomId;
    private Long senderId;
    private String nickname;
    private String content;

}
