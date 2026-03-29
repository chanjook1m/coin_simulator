package com.coin.simulator.domain.price.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class CoinNotExistException extends BusinessException {
    public CoinNotExistException(String symbol) {
        super(ErrorCode.COIN_NOT_FOUND, String.format(ErrorCode.COIN_NOT_FOUND.getMessage(), symbol));
    }
}
