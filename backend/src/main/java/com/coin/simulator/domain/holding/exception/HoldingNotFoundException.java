package com.coin.simulator.domain.holding.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class HoldingNotFoundException extends BusinessException {
    public HoldingNotFoundException(Long walletId, Long coinId) {
        super(ErrorCode.HOLDING_NOT_FOUND, String.format(ErrorCode.HOLDING_NOT_FOUND.getMessage(), walletId, coinId));
    }
}
