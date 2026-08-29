package com.nguyenhien.shared.exception.base;

import com.nguyenhien.shared.exception.model.ErrorCode;

public class BusinessException extends BaseException{
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
