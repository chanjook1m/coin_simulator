package com.coin.simulator.domain.price.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class EmptySymbolException extends BusinessException {
    public EmptySymbolException() {
        super(ErrorCode.EMPTY_SYMBOL, String.format(ErrorCode.EMPTY_SYMBOL.getMessage()));
    }
}
