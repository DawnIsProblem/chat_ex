package com.chat.ex.domain.chat.controller;

import com.chat.ex.common.response.CommonResponse;
import com.chat.ex.domain.chat.dto.request.CreateUserRequestDto;
import com.chat.ex.domain.chat.dto.response.CreateUserResponseDto;
import com.chat.ex.domain.chat.service.ChatUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "사용자 관련 API")
public class ChatUserController {

    private final ChatUserService chatUserService;

    @PostMapping
    @Operation(summary = "닉네임 생성 API", description = "닉네임을 입력해서 채팅 서비스를 이용할 준비를 합니다.")
    public CommonResponse<CreateUserResponseDto> create(
            @RequestBody CreateUserRequestDto request
    ){
        CreateUserResponseDto response = chatUserService.create(request);
        return CommonResponse.success("사용자 생성 성공!", response);
    }

}
