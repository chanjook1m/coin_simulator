package com.coin.simulator.infrastructure.exchange;

public class ExternalExchangeException extends RuntimeException{
    public ExternalExchangeException(String message) {
        super(message);
    }

    public ExternalExchangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
