package com.chat.ex.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonResponse<T> {

    private int status;
    private String message;
    private String timestamp;
    private T data;

    public static <T> CommonResponse<T> success(String message, T data) {
        return CommonResponse.<T>builder()
                .status(200)
                .message(message)
                .data(data)
                .timestamp(java.time.OffsetDateTime.now().toString())
                .build();
    }

    public static <T> CommonResponse<T> success(String message) {
        return success(message, null);
    }

    public static <T> CommonResponse<T> failure(int status, String message) {
        return CommonResponse.<T>builder()
                .status(status)
                .message(message)
                .data(null)
                .timestamp(java.time.OffsetDateTime.now().toString())
                .build();
    }

}
