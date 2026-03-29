package com.coin.simulator.common;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;
import com.coin.simulator.infrastructure.exchange.exception.ExchangeConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("BusinessException: code={}, message={}", errorCode.getCode(), e.getMessage());

        ErrorResponse body = ErrorResponse.of(errorCode, e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }

    @ExceptionHandler(ExchangeConnectionException.class)
    public ResponseEntity<ErrorResponse> handleExternal(ExchangeConnectionException e) {
        ErrorCode errorCode = ErrorCode.EXTERNAL_EXCHANGE_ERROR;
        log.error("ExternalExchangeException: {}", e.getMessage(), e);

        ErrorResponse body = ErrorResponse.of(errorCode, null);
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Unexpected error", e);

        ErrorResponse body = ErrorResponse.of(errorCode, null);
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }

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

}
