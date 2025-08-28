package com.chat.ex.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinRoomRequestDto {

    @Schema(description = "방 코드", example = "asdasd22")
    private String roomCode;

    @Schema(description = "사용자 아이디", example = "1")
    private Long userId;

}
