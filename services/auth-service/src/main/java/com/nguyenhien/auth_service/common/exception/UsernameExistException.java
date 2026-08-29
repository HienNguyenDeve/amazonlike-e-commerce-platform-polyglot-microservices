package com.nguyenhien.auth_service.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsernameExistException extends RuntimeException{
    public UsernameExistException() {
        super("Username is existed");
    }
}
