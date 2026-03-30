package com.coin.simulator.common.exception;

public class ExternalException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String externalMessage; // 외부 시스템에서 보내온 원본 메시지

    public ExternalException(ErrorCode errorCode, String externalMessage) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.externalMessage = externalMessage;
    }

    public ExternalException(ErrorCode errorCode, String externalMessage, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.externalMessage = externalMessage;
    }
}
