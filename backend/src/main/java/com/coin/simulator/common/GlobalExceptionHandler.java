package com.coin.simulator.common;

import com.coin.simulator.common.exception.BadRequestException;
import com.coin.simulator.common.exception.NotFoundException;
import com.coin.simulator.infrastructure.exchange.ExternalExchangeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(ExternalExchangeException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseEntity<ErrorResponse> handleExternal(ExternalExchangeException e) {
        log.error("외부 거래소 연동 실패", e);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(ErrorResponse.of(HttpStatus.BAD_GATEWAY, "외부 거래소 연동 중 오류가 발생했습니다"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
        log.error("예상치 못한 서버 오류");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다"));
    }

    public record ErrorResponse(
            int status,
            String error,
            String message,
            String timestamp
    ) {
        public static ErrorResponse of(HttpStatus status, String message) {
            return new ErrorResponse(
                    status.value(),
                    status.getReasonPhrase(),
                    message,
                    LocalDateTime.now().toString()
            );
        }
    }

}
