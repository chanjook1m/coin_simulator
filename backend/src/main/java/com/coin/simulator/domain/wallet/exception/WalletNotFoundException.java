package com.coin.simulator.domain.wallet.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class WalletNotFoundException extends BusinessException {
    public WalletNotFoundException(Long userId) {
        super(ErrorCode.WALLET_NOT_FOUND, String.format(ErrorCode.WALLET_NOT_FOUND.getMessage(), userId));
    }
}
