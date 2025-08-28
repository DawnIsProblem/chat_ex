package com.chat.ex.domain.chat.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LeaveRoomRequestDto {

    private Long roomId;
    private Long userId;

}
