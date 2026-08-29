package com.nguyenhien.shared.exception.model;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    RESOURCE_NOT_FOUND("ERR_404", "The requested resource was not found", HttpStatus.NOT_FOUND),
    RESOURCE_ALREADY_EXISTS("ERR_409", "The resource already exists", HttpStatus.CONFLICT),
    INVALID_STATE("ERR_400", "The system is in an invalid state for this operation", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("ERR_401", "Full authentication is required to access this resource", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("ERR_403", "You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    VALIDATION_ERROR("ERR_422", "Validation failed for the provided input", HttpStatus.UNPROCESSABLE_ENTITY),
    INTERNAL_ERROR("ERR_500", "An unexpected error occurred on the server", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
