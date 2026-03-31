package com.coin.simulator.domain.wallet.exception;

import com.coin.simulator.common.exception.ErrorCode;
import com.coin.simulator.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.coin.simulator.domain.wallet.controller")
public class WalletExceptionHandler {

    @ExceptionHandler(InSufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(InSufficientBalanceException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("InsufficientBalanceException: {}", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAmount(InvalidAmountException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("InvalidAmountException: {}", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWalletNotFound(WalletNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("WalletNotFoundException: {}", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }
}