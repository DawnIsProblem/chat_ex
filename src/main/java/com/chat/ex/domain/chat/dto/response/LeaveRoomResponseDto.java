package com.chat.ex.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LeaveRoomResponseDto {

    private Long roomId;
    private Long userId;

}
