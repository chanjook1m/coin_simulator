package com.coin.simulator.domain.wallet.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class InSufficientBalanceException extends BusinessException {
    public InSufficientBalanceException() {
        super(ErrorCode.INSUFFICIENT_BALANCE, String.format(ErrorCode.INSUFFICIENT_BALANCE.getMessage()));
    }
}
