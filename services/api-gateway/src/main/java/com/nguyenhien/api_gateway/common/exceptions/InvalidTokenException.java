package com.nguyenhien.api_gateway.common.exceptions;

public class InvalidTokenException extends RuntimeException {
  public InvalidTokenException() {
    super("JWT token is invalid.");
  }

  public InvalidTokenException(String message) {
    super(message);
  }
}
