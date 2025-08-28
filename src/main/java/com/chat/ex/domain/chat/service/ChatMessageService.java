package com.chat.ex.domain.chat.service;

import com.chat.ex.common.exception.CustomException;
import com.chat.ex.domain.chat.dto.request.ChatMessageRequestDto;
import com.chat.ex.domain.chat.dto.response.ChatMessageResponseDto;
import com.chat.ex.domain.chat.entity.ChatMessage;
import com.chat.ex.domain.chat.entity.ChatRoom;
import com.chat.ex.domain.chat.entity.ChatUser;
import com.chat.ex.domain.chat.error.ChatError;
import com.chat.ex.domain.chat.repository.ChatMessageRepository;
import com.chat.ex.domain.chat.repository.ChatRoomMemberRepository;
import com.chat.ex.domain.chat.repository.ChatRoomRepository;
import com.chat.ex.domain.chat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public ChatMessageResponseDto save(ChatMessageRequestDto request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new CustomException(ChatError.ROOM_NOT_FOUND));

        ChatUser chatUser = chatUserRepository.findById(request.getSenderId())
                .orElseThrow(() -> new CustomException(ChatError.USER_NOT_FOUND));

        boolean isMember = chatRoomMemberRepository.existsByRoomIdAndUserId(request.getRoomId(), request.getSenderId());

        if (!isMember) {
            throw new CustomException(ChatError.JOIN_DENIED);
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .type(request.getMessageType())
                .room(chatRoom)
                .sender(chatUser)
                .nickname(request.getNickname())
                .content(request.getContent())
                .build();

        chatMessageRepository.save(chatMessage);

        return ChatMessageResponseDto.builder()
                .id(chatMessage.getId())
                .messageType(chatMessage.getType())
                .roomId(chatMessage.getRoom().getId())
                .senderId(chatMessage.getSender().getId())
                .nickname(chatMessage.getNickname())
                .content(chatMessage.getContent())
                .build();


    }

}
