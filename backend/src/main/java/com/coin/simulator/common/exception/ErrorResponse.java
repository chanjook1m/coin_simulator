package com.coin.simulator.common.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String timestamp
) {
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(
                errorCode.getStatus().value(),
                errorCode.getCode(),
                message != null ? message : errorCode.getMessage(),
                LocalDateTime.now().toString()
        );
    }
}
