package com.nguyenhien.user_service.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PreferenceNotFoundException extends RuntimeException{
    public PreferenceNotFoundException() {
        super("Preference not found");
    }
}
