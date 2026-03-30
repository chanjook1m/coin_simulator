package com.coin.simulator.infrastructure.exchange.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class MarketDataEmptyException extends BusinessException {
    public MarketDataEmptyException() {
        super(ErrorCode.EXCHANGE_MARKET_DATA_NOT_FOUND, String.format(ErrorCode.EXCHANGE_MARKET_DATA_NOT_FOUND.getMessage()));
    }
}
