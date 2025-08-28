package com.chat.ex.domain.chat.service;

import com.chat.ex.common.exception.CustomException;
import com.chat.ex.domain.chat.dto.request.CreateChatRoomRequestDto;
import com.chat.ex.domain.chat.dto.request.JoinRoomRequestDto;
import com.chat.ex.domain.chat.dto.request.LeaveRoomRequestDto;
import com.chat.ex.domain.chat.dto.response.ChatMessageResponseDto;
import com.chat.ex.domain.chat.dto.response.CreateChatRoomResponseDto;
import com.chat.ex.domain.chat.dto.response.JoinRoomResponseDto;
import com.chat.ex.domain.chat.dto.response.LeaveRoomResponseDto;
import com.chat.ex.domain.chat.entity.ChatMessage;
import com.chat.ex.domain.chat.entity.ChatRoom;
import com.chat.ex.domain.chat.entity.ChatRoomMember;
import com.chat.ex.domain.chat.entity.ChatUser;
import com.chat.ex.domain.chat.enums.MessageType;
import com.chat.ex.domain.chat.repository.ChatMessageRepository;
import com.chat.ex.domain.chat.repository.ChatRoomMemberRepository;
import com.chat.ex.domain.chat.repository.ChatRoomRepository;
import com.chat.ex.domain.chat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

import static com.chat.ex.domain.chat.error.ChatError.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatUserRepository chatUserRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private static final char[] ALPH = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();
    private final SecureRandom rnd = new SecureRandom();

    public CreateChatRoomResponseDto create(CreateChatRoomRequestDto request) {
        ChatUser hostUser = chatUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        for (int attempt = 0; attempt < 5; attempt++) {
            String code = random(6);
            try {
                ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                        .roomCode(code)
                        .host(hostUser)
                        .build()
                );
                chatRoomMemberRepository.save(ChatRoomMember.builder()
                        .room(chatRoom)
                        .user(hostUser)
                        .nickname(hostUser.getNickname())
                        .build()
                );
                return CreateChatRoomResponseDto.builder()
                        .roomId(chatRoom.getId())
                        .roomCode(chatRoom.getRoomCode())
                        .hostId(hostUser.getId())
                        .hostNickname(hostUser.getNickname())
                        .build();

            } catch (DataIntegrityViolationException dup) {
                if (attempt == 4) {
                    throw dup;
                }
            }
        }
        throw new CustomException(FAIL_CREATE_ROOM_CODE);
    }

    public JoinRoomResponseDto join(JoinRoomRequestDto request) {
        // 1) 방, 유저 로드
        ChatRoom room = chatRoomRepository.findByRoomCode(request.getRoomCode())
                .orElseThrow(() -> new CustomException(ROOM_NOT_FOUND));

        ChatUser user = chatUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 2) 멤버십 확인
        var existing = chatRoomMemberRepository.findByRoomIdAndUserId(room.getId(), user.getId());

        if (existing.isEmpty()) {
            // 미가입 → 가입
            chatRoomMemberRepository.save(
                    ChatRoomMember.builder()
                            .room(room)
                            .user(user)
                            .nickname(user.getNickname())
                            .build()
            );
        }

        // 3) 응답 (roomId 포함)
        return JoinRoomResponseDto.builder()
                .roomId(room.getId())
                .roomCode(room.getRoomCode())
                .guestId(user.getId())
                .guestNickname(user.getNickname())
                .build();
    }

    public LeaveRoomResponseDto leave(LeaveRoomRequestDto request) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByRoomIdAndUserId(request.getRoomId(), request.getUserId())
                .orElseThrow(() -> new CustomException(USER_OR_ROOM_NOT_FOUND));

        if (chatRoomMember.getLeftAt() == null) {
            chatRoomMember.left();
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .type(MessageType.LEAVE)
                .room(chatRoomMember.getRoom())
                .sender(chatRoomMember.getUser())
                .nickname(chatRoomMember.getNickname())
                .content(chatRoomMember.getNickname() + "님이 퇴장했습니다.")
                .build();

        chatMessageRepository.save(chatMessage);

        var payload = ChatMessageResponseDto.builder()
                .id(chatMessage.getId())
                .messageType(chatMessage.getType())
                .roomId(chatRoomMember.getRoom().getId())
                .senderId(chatRoomMember.getUser().getId())
                .nickname(chatMessage.getNickname())
                .content(chatMessage.getContent())
                .build();

        simpMessagingTemplate.convertAndSend("/topic/room." + request.getRoomId(), payload);

        return LeaveRoomResponseDto.builder()
                .roomId(chatMessage.getRoom().getId())
                .userId(chatRoomMember.getUser().getId())
                .build();
    }

    private String random(int n) {
        var sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(ALPH[rnd.nextInt(ALPH.length)]);
        return sb.toString();
    }

}
