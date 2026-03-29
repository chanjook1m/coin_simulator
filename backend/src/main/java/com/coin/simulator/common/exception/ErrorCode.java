package com.coin.simulator.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // --- Common & Server (S) ---
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 내부 오류가 발생했습니다."),

    // --- Wallet (W) ---
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "W001", "%s 금액은 0보다 커야 합니다"),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "W002", "잔액이 부족합니다"),
    WALLET_NOT_FOUND(HttpStatus.NOT_FOUND, "W003", "사용자(ID: %s)의 지갑 정보를 찾을 수 없습니다"),

    // --- USER (U) ---
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),

    // --- ORDER (O) ---
    INVALID_ORDER_QUANTITY(HttpStatus.BAD_REQUEST, "O001", "주문 수량은 0보다 커야 합니다."),

    // --- Coin & Price (C, P) ---
    COIN_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "존재하지 않는 코인입니다"),
    COIN_PRICE_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "코인 시세를 찾을 수 없습니다."),
    EMPTY_SYMBOL(HttpStatus.BAD_REQUEST, "R001", "심볼은 필수 값입니다."),

    // --- External Exchange (E) ---
    EXTERNAL_EXCHANGE_ERROR(HttpStatus.BAD_GATEWAY, "E001", "외부 거래소 연동 중 오류가 발생했습니다."),
    EXCHANGE_MARKET_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "E002", "거래소에서 지원하는 코인 목록을 찾을 수 없습니다"),
    EXCHANGE_MARKET_CONNECTION_ERROR(HttpStatus.BAD_GATEWAY, "E003", "거래소 통신 중 오류가 발생했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
