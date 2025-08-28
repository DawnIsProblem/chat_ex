package com.chat.ex.domain.chat.controller;

import com.chat.ex.common.response.CommonResponse;
import com.chat.ex.domain.chat.dto.request.CreateChatRoomRequestDto;
import com.chat.ex.domain.chat.dto.request.JoinRoomRequestDto;
import com.chat.ex.domain.chat.dto.request.LeaveRoomRequestDto;
import com.chat.ex.domain.chat.dto.response.CreateChatRoomResponseDto;
import com.chat.ex.domain.chat.dto.response.JoinRoomResponseDto;
import com.chat.ex.domain.chat.dto.response.LeaveRoomResponseDto;
import com.chat.ex.domain.chat.entity.ChatRoom;
import com.chat.ex.domain.chat.service.ChatRoomService;
import com.chat.ex.domain.chat.service.ChatUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@Tag(name = "채팅 및 채팅방 관련 API")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatUserService chatUserService;

    @PostMapping
    @Operation(summary = "채팅방 생성 API", description = "사용자가 채팅방을 생성합니다.")
    public CommonResponse<CreateChatRoomResponseDto> create(
            @RequestBody CreateChatRoomRequestDto request
    ) {
        CreateChatRoomResponseDto response = chatRoomService.create(request);
        return CommonResponse.success("채팅방 생성 성공!", response);
    }

    @PostMapping("/join")
    @Operation(summary = "채팅방 입장 API", description = "사용자가 채팅방에 입장합니다.")
    public CommonResponse<JoinRoomResponseDto> join(
            @RequestBody JoinRoomRequestDto request
    ) {
        JoinRoomResponseDto response = chatRoomService.join(request);
        return CommonResponse.success("채팅방 입장 성공!", response);
    }

    @PostMapping("/leave")
    @Operation(summary = "채팅방 퇴장 API", description = "사용자가 채팅방에서 퇴장합니다.")
    public CommonResponse<LeaveRoomResponseDto> leave(
            @RequestBody LeaveRoomRequestDto request
    ) {
        LeaveRoomResponseDto response = chatRoomService.leave(request);
        return CommonResponse.success("채팅방 퇴장 성공!", response);
    }

}
