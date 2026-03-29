package com.coin.simulator.domain.order.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class InvalidOrderQuantityException extends BusinessException {
    public InvalidOrderQuantityException() {
        super(ErrorCode.INVALID_ORDER_QUANTITY, String.format(ErrorCode.INVALID_ORDER_QUANTITY.getMessage()));
    }
}
