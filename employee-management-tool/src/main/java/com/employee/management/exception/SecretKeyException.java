package com.employee.management.exception;

public class SecretKeyException extends RuntimeException {

  public SecretKeyException(String message) {
    super(message);
  }

  public SecretKeyException(String message, Throwable cause) {
    super(message, cause);
  }
}
