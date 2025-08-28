package com.chat.ex.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JoinRoomResponseDto {

    private Long roomId;
    private String roomCode;
    private Long guestId;
    private String guestNickname;

}
