package com.coin.simulator.domain.order.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;
import com.coin.simulator.domain.order.entity.OrderStatus;

public class InvalidOrderStatusException extends BusinessException {
    public InvalidOrderStatusException(OrderStatus currentStatus) {
        super(ErrorCode.INVALID_ORDER_STATUS, String.format(ErrorCode.INVALID_ORDER_STATUS.getMessage(), currentStatus));
    }
}
