package com.coin.simulator.domain.order.exception;

import com.coin.simulator.common.exception.ErrorCode;
import com.coin.simulator.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.coin.simulator.domain.order.controller")
public class OrderExceptionHandler {

    @ExceptionHandler(InvalidExecutionPriceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidExecutionPrice(InvalidExecutionPriceException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("InvalidExecutionPriceException: {}", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }

    @ExceptionHandler(InvalidLimitPriceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLimitPrice(InvalidLimitPriceException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("InvalidLimitPriceException: {}", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }

    @ExceptionHandler(InvalidOrderQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrderQuantity(InvalidOrderQuantityException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("InvalidOrderQuantityException: {}", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrderStatus(InvalidOrderStatusException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("InvalidOrderStatusException: {}", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }
}