package com.nguyenhien.shared.exception.base;

import com.nguyenhien.shared.exception.model.ErrorCode;

public class SystemException extends BaseException{
    private final ErrorCode errorCode;

    protected SystemException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
