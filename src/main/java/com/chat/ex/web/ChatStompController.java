package com.chat.ex.web;

import com.chat.ex.domain.chat.dto.request.ChatMessageRequestDto;
import com.chat.ex.domain.chat.enums.MessageType;
import com.chat.ex.domain.chat.service.ChatMessageService;
import com.chat.ex.domain.chat.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final SimpMessagingTemplate template;
    private final ChatMessageService chatMessageService;
    private final ChatUserService chatUserService;

    @MessageMapping("/chat.join")
    public void join(
            @Payload ChatMessageRequestDto dto
    ) {
        var user = chatUserService.require(dto.getSenderId());
        var saved = chatMessageService.save(ChatMessageRequestDto.builder()
                .messageType(MessageType.JOIN)
                .roomId(dto.getRoomId())
                .senderId(user.getId())
                .nickname(user.getNickname())
                .content(user.getNickname() + "님이 입장했습니다.")
                .build());
        template.convertAndSend("/topic/room." + saved.getRoomId(), saved);
    }

    @MessageMapping("/chat.send")
    public void send(
            @Payload ChatMessageRequestDto dto
    ) {
        var user = chatUserService.require(dto.getSenderId());
        var saved = chatMessageService.save(ChatMessageRequestDto.builder()
                .messageType(MessageType.TALK)
                .roomId(dto.getRoomId())
                .senderId(user.getId())
                .nickname(user.getNickname())
                .content(dto.getContent())
                .build());
        template.convertAndSend("/topic/room." + saved.getRoomId(), saved);
    }

    @MessageMapping("/chat.leave")
    public void leave(
            @Payload ChatMessageRequestDto dto
    ) {
        var user = chatUserService.require(dto.getSenderId());
        var saved = chatMessageService.save(ChatMessageRequestDto.builder()
                .messageType(MessageType.LEAVE)
                .roomId(dto.getRoomId())
                .senderId(user.getId())
                .nickname(user.getNickname())
                .content(user.getNickname() + "님이 퇴장했습니다.")
                .build());
        template.convertAndSend("/topic/room." + saved.getRoomId(), saved);
    }

}
