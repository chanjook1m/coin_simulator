package com.coin.simulator.infrastructure.exchange.exception;

import com.coin.simulator.common.exception.ErrorCode;
import com.coin.simulator.common.exception.ExternalException;

public class ExchangeConnectionException extends ExternalException {
    public ExchangeConnectionException(Throwable cause) {
        super(ErrorCode.EXCHANGE_MARKET_CONNECTION_ERROR, String.format(ErrorCode.EXCHANGE_MARKET_CONNECTION_ERROR.getMessage()), cause);
    }
}
