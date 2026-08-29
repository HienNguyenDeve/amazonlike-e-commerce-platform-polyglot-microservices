package com.nguyenhien.api_gateway.common.exceptions;

public class ExpiredTokenException extends RuntimeException {
  public ExpiredTokenException() {
    super("JWT token has expired.");
  }

  public ExpiredTokenException(String message) {
    super(message);
  }
}
