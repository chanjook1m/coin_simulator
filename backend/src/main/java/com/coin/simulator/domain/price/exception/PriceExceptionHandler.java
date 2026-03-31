package com.coin.simulator.domain.price.exception;

import com.coin.simulator.common.exception.ErrorCode;
import com.coin.simulator.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.coin.simulator.domain.price.api")
public class PriceExceptionHandler {

    @ExceptionHandler(EmptySymbolException.class)
    public ResponseEntity<ErrorResponse> handlePriceNotFound(EmptySymbolException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("EmptySymbolException: {}", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }
}