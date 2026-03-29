package com.coin.simulator.domain.coin.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class CoinNotFoundException extends BusinessException {
    public CoinNotFoundException(String symbol) {
        super(ErrorCode.COIN_NOT_FOUND, String.format(ErrorCode.COIN_NOT_FOUND.getMessage(), symbol));
    }
}
