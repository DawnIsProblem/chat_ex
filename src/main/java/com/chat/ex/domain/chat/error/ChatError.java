package com.chat.ex.domain.chat.error;

import com.chat.ex.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatError implements ErrorCode {

    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND,  "C001", "채팅방을 찾을 수 없습니다."),
    FAIL_CREATE_ROOM_CODE(HttpStatus.CONFLICT, "C002", "방 코드 생성을 실패했습니다.."),
    JOIN_DENIED(HttpStatus.FORBIDDEN,     "C003", "채팅방 참여가 거부되었습니다."),
    INVALID_ROOM_CODE(HttpStatus.BAD_REQUEST, "C004", "잘못된 초대 코드입니다."),
    EMPTY_MESSAGE(HttpStatus.BAD_REQUEST, "C005", "메시지가 비어 있습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "C006", "사용자를 찾을 수 없습니다."),
    USER_OR_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "C007", "사용자나 방을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
