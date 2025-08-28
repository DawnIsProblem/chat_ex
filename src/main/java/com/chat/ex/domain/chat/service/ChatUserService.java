package com.chat.ex.domain.chat.service;

import com.chat.ex.common.exception.CustomException;
import com.chat.ex.domain.chat.dto.request.CreateUserRequestDto;
import com.chat.ex.domain.chat.dto.response.CreateUserResponseDto;
import com.chat.ex.domain.chat.entity.ChatUser;
import com.chat.ex.domain.chat.repository.ChatUserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.chat.ex.domain.chat.error.ChatError.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatUserService {

    private final ChatUserRepository chatUserRepository;

    public CreateUserResponseDto create(CreateUserRequestDto request) {

        ChatUser chatUser = ChatUser.builder()
                .nickname(request.getNickname())
                .build();

        chatUserRepository.save(chatUser);

        return CreateUserResponseDto.builder()
                .id(chatUser.getId())
                .nickname(chatUser.getNickname())
                .build();
    }

    public ChatUser require(Long id) {
        return chatUserRepository.findById(id)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

}
