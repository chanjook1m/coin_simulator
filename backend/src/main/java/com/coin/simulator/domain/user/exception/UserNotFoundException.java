package com.coin.simulator.domain.user.exception;

import com.coin.simulator.common.exception.BusinessException;
import com.coin.simulator.common.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND, String.format(ErrorCode.USER_NOT_FOUND.getMessage()));
    }
}
