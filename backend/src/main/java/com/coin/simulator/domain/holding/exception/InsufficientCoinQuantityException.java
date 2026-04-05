package com.coin.simulator.domain.holding.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class InsufficientCoinQuantityException extends BusinessException {
    public InsufficientCoinQuantityException(Long userId, String symbol) {
        super(ErrorCode.INSUFFICIENT_COIN_QUANTITY, String.format(ErrorCode.INSUFFICIENT_COIN_QUANTITY.getMessage(), userId, symbol));
    }
}
