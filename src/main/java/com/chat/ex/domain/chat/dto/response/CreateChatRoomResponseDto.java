package com.chat.ex.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateChatRoomResponseDto {

    private Long roomId;
    private String roomCode;
    private Long hostId;
    private String hostNickname;

}
