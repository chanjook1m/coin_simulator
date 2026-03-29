package com.coin.simulator.domain.order.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class InvalidOrderQuantity extends BusinessException {
    public InvalidOrderQuantity() {
        super(ErrorCode.INVALID_ORDER_QUANTITY, String.format(ErrorCode.INVALID_ORDER_QUANTITY.getMessage()));
    }
}
