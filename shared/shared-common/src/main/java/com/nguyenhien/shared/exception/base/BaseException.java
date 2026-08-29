package com.nguyenhien.shared.exception.base;

import com.nguyenhien.shared.exception.model.ErrorCode;

public class BaseException extends RuntimeException{
    private final ErrorCode errorCode;

    protected BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
