package com.coin.simulator.domain.wallet.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class InvalidAmountException extends BusinessException {
    public InvalidAmountException(String type) {
        super(ErrorCode.INVALID_AMOUNT, String.format(ErrorCode.INVALID_AMOUNT.getMessage(), type));
    }
}
