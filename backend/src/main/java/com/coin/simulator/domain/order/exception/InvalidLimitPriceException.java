package com.coin.simulator.domain.order.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class InvalidLimitPriceException extends BusinessException {
    public InvalidLimitPriceException() {
        super(ErrorCode.INVALID_LIMIT_PRICE, String.format(ErrorCode.INVALID_LIMIT_PRICE.getMessage()));
    }
}
