package com.nguyenhien.api_gateway.common.exceptions;

public class MissingTokenException extends RuntimeException {
  public MissingTokenException() {
    super("Missing token");
  }

  public MissingTokenException(String message) {
    super(message);
  }
}
