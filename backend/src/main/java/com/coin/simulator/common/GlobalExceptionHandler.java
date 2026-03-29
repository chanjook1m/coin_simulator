package com.coin.simulator.common;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;
import com.coin.simulator.infrastructure.exchange.exception.ExchangeConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseException(HttpMessageNotReadableException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "INVALID_JSON_FORMAT");
        response.put("message", "JSON 형식이 올바르지 않습니다. 따옴표나 쉼표를 확인하세요.");

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException e) {

        ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

        log.warn("잘못된 경로 요청: {}", e.getMessage());

        ErrorResponse body = ErrorResponse.of(errorCode, "요청하신 경로를 찾을 수 없습니다: " + e.getResourcePath());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeader(MissingRequestHeaderException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        log.warn("필수 헤더 누락: {} (Header Name: {})", e.getMessage(), e.getHeaderName());

        ErrorResponse body = ErrorResponse.of(errorCode,
                String.format("필수 헤더 '%s'가 누락되었습니다.", e.getHeaderName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
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
