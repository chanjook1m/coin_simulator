package com.coin.simulator.domain.order.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class InvalidExecutionPriceException extends BusinessException {
    public InvalidExecutionPriceException() {
        super(ErrorCode.INVALID_EXECUTION_PRICE);
    }
}
