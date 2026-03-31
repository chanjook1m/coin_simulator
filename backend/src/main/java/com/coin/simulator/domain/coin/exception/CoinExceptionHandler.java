package com.coin.simulator.domain.coin.exception;

import com.coin.simulator.common.exception.ErrorCode;
import com.coin.simulator.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.coin.simulator.domain.coin.controller")
public class CoinExceptionHandler {

    @ExceptionHandler(CoinNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCoinNotFound(CoinNotFoundException e) {
        ErrorCode errorCode = ErrorCode.COIN_NOT_FOUND;
        log.warn("CoinNotFound: ", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }

    @ExceptionHandler(CoinPriceNotExistException.class)
    public ResponseEntity<ErrorResponse> handleCoinPriceNotExist(CoinPriceNotExistException e) {
        ErrorCode errorCode = ErrorCode.COIN_PRICE_NOT_FOUND;
        log.warn("CoinPriceNotExist: ", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }
}
