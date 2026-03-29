package com.coin.simulator.domain.price.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class CoinPriceNotExistException extends BusinessException {
    public CoinPriceNotExistException(String symbol) {
        super(ErrorCode.COIN_PRICE_NOT_FOUND, String.format(ErrorCode.COIN_PRICE_NOT_FOUND.getMessage(), symbol));
    }
}
